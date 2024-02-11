package org.example.ServiceImpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Slf4j
@Service
public class ProxySetup {

    private List<String> ips;

    private Thread ipThread ;

    public ProxySetup() {
        ips = new ArrayList<>();
        ipThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        writeIps();
                    } catch (Exception e) {
                        log.info("exception found in writeIps");
                    }
                    try {
                        Thread.sleep(180000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

            }
        });
        ipThread.start();
    }

    @Autowired
    private RestTemplate restTemplate;

    ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public String getIp() {
//        log.info("get IP");
        readWriteLock.readLock().lock();
        int index = ThreadLocalRandom.current().nextInt(0, ips.size()-1);
        String ip = ips.get(index);
        if (!StringUtils.hasLength(ip)) {
            log.info("ip {} index {} ipSize {} ipList {}",ip,index,ips.size(),ips);
        }
        readWriteLock.readLock().unlock();
//        log.info("get IP {}",ip);
        return ip;
    }

    public void writeIps() {
        log.info("getting ips");
        String res = new RestTemplate()
                .getForEntity("https://api.proxyscrape.com/v3/free-proxy-list/get?request=displayproxies&protocol=http&timeout=15000&proxy_format=ipport&format=text",
                        String.class
                ).getBody();

        readWriteLock.writeLock().lock();
        ips.clear();
        Scanner sc = new Scanner(res);
        while (sc.hasNext()) {
            String ip = sc.nextLine();
            ips.add(ip);
        }
        readWriteLock.writeLock().unlock();
        log.info("successfully fetched ips");
    }


}
