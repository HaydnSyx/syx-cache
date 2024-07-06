package cn.syx.cache.core;

import cn.syx.cache.domain.CacheTask;
import cn.syx.cache.domain.CacheTimeEntity;
import cn.syx.cache.utils.SingletonUtil;
import io.github.haydnsyx.toolbox.base.NumberTool;
import io.github.haydnsyx.toolbox.base.ThreadTool;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.TimeUnit;

@Slf4j
public class SyxCacheTimeWheel {

    private final long BASE_LINE_TIME;
    private Thread wheelThread;

    public SyxCacheTimeWheel() {
        LocalDateTime localDateTime = LocalDateTime.of(2024, 6, 1, 0, 0, 0, 0);
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
        BASE_LINE_TIME = zonedDateTime.toInstant().toEpochMilli();
    }

    private final PriorityQueue<CacheTimeEntity>[] wheel = new PriorityQueue[60];

    public void init() {
        for (int i = 0; i < wheel.length; i++) {
            wheel[i] = new PriorityQueue<>(Comparator.comparingLong(CacheTimeEntity::getTurnNum));
        }

        TimeWheelExpireTask wheelTask = new TimeWheelExpireTask();
        wheelThread = new Thread(wheelTask, "cache-wheel-task");
    }

    public void start() {
        wheelThread.start();
    }

    public void add(int dbNum, String key, long time) {
        long diff = time - BASE_LINE_TIME;
        // 计算索引
        int index = (int) (diff / 1000 % 60);

        // 计算轮次
        long turnNum = diff / 1000 / 60;

        // 插入到对应的列表中
        wheel[index].offer(new CacheTimeEntity(dbNum, key, turnNum));
        log.info("wheel add new dbNum, dbNum:{}, key:{}, index:{}, turnNum:{}", dbNum, key, index, turnNum);
    }

    public class TimeWheelExpireTask implements Runnable {

        @Override
        public void run() {
            while (true) {
                long now = System.currentTimeMillis();
                try {
                    long diff = now - BASE_LINE_TIME;
                    // 计算索引
                    int index = (int) (diff / 1000 % 60);
//                    log.info("wheel task current calc index:{}", index);
                    PriorityQueue<CacheTimeEntity> queue = wheel[index];
                    // 没有过期的数据
                    if (queue.isEmpty()) {
                        continue;
                    }

                    long turnNum = diff / 1000 / 60;
                    while (!queue.isEmpty()) {
                        // 查看数据
                        CacheTimeEntity entity = queue.peek();
                        // 小于等于当前轮次选出来准备删除
                        if (turnNum >= entity.getTurnNum()) {
                            // 弹出该数据
                            queue.poll();

                            CacheTask cacheTask = CacheTask.create(entity.getDbNum(), new String[]{"delete", entity.getKey()});
                            SingletonUtil.getInstance(SyxCacheTask.class).submitTask(cacheTask);

                            log.info("wheel task get expired index:{}, turnNum:{}, key:{}, dbNum:{}",
                                    index, turnNum, entity.getKey(), entity.getDbNum());
                        } else {
                            break;
                        }
                    }
                } catch (Exception e) {
                    log.error("wheel task execute error", e);
                } finally {
                    long end = System.currentTimeMillis();
                    long diff = NumberTool.INT_ONE_THOUSAND - (end - now);
                    ThreadTool.sleep(NumberTool.isPositive(diff) ? diff : NumberTool.LONG_ONE_THOUSAND, TimeUnit.MILLISECONDS);
                }
            }
        }
    }
}
