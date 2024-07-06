package cn.syx.cache.core;

import cn.syx.cache.db.SyxCacheDb;
import io.github.haydnsyx.toolbox.base.ThreadTool;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.util.RamUsageEstimator;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

@Slf4j
public class SyxCacheMonitor {

    private final SyxCacheDb[] DBS;
    private Thread monitorThread;

    private boolean checkFlag;

    // 使用堆内内存来模仿redis内存淘汰机制
    // jvm最大内存，即-Xmx
    private long maxMemory;
    private float warnClosePercent = 0.8f;
    // db中所有map总size达到的阈值比列开启警告（默认80%）
    private float warnOpenPercent = 0.85f;
    // db中所有map总size达到的阈值比列开启淘汰机制（默认90%）
    private float limitPercent = 0.9f;
    // 根据阈值比例计算出来的db总预警内存量
    private long warnMinMemory;
    private long warnMaxMemory;
    // 根据阈值比例计算出来的db总限制内存量
    private long limitMemory;

    public SyxCacheMonitor(SyxCacheDb[] DBS) {
        this.DBS = DBS;
    }

    public void init() {
        Runtime runtime = Runtime.getRuntime();
        maxMemory = runtime.maxMemory();
        limitMemory = new BigDecimal(maxMemory).multiply(new BigDecimal(limitPercent)).longValue();

        warnMinMemory = new BigDecimal(maxMemory).multiply(new BigDecimal(warnClosePercent)).longValue();
        warnMaxMemory = new BigDecimal(maxMemory).multiply(new BigDecimal(warnOpenPercent)).longValue();

        MonitorTask task = new MonitorTask();
        monitorThread = new Thread(task, "cache-monitor-task");
    }

    public void start() {
        monitorThread.start();
    }

    public int totalKeySize() {
        int size = 0;
        for (SyxCacheDb db : DBS) {
            size = size + db.getMap().size();
        }
        return size;
    }

    public long totalUseMemory() {
        long size = 0;
        for (SyxCacheDb db : DBS) {
            size = size + db.totalMemorySize();
        }
        return size;
    }

    public boolean isOverLimit() {
        return totalUseMemory() >= limitMemory;
    }

    public boolean isOverLimit(Object object) {
        return (totalUseMemory() + RamUsageEstimator.sizeOfObject(object)) >= limitMemory;
    }

    public class MonitorTask implements Runnable {

        @Override
        public void run() {
            while (true) {
                long currentDbSize = totalUseMemory();
                if (!checkFlag && currentDbSize >= warnMaxMemory) {
                    log.warn("cache db total memory size over warn limit, start to warn ...");
                    checkFlag = true;
                } else if (checkFlag && currentDbSize < warnMinMemory) {
                    log.warn("cache db total memory size close warn limit, stop warn ...");
                    checkFlag = false;
                }

                log.info(" ===> cache db total memory size:{}, warn min memory:{}, warn max memory:{}, limit memory:{}, flag:{}",
                        currentDbSize, warnMinMemory, warnMaxMemory, limitMemory, checkFlag);
                long time;
                // 已经进入到高内存使用场景，加快检查频率
                if (checkFlag) {
                    time = 500L;
                }
                // 进入到次预警区，进行每秒检查
                else if (currentDbSize >= warnMinMemory) {
                    time = 1000L;
                }
                // 低预警区，按照key的数量粗略处理
                else {
                    int keySize = totalKeySize();
                    if (keySize > 10000) {
                        time = 3000L;
                    }
                    // 低预警区，按照key的数量粗略处理
                    else if (keySize > 5000) {
                        time = 5000L;
                    } else {
                        time = 10_000L;
                    }
                }
                ThreadTool.sleep(time, TimeUnit.MILLISECONDS);
            }
        }
    }
}
