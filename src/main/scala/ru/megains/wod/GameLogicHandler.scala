package ru.megains.wod

import java.util.concurrent.{ConcurrentLinkedQueue, ExecutorService, Executors}
import java.util.concurrent.locks.ReentrantReadWriteLock



class GameLogicHandler(server: WoDServer) extends Runnable with Logger[GameLogicHandler]{



    val poolSize:Int = 10
    val pool: ExecutorService = Executors.newFixedThreadPool(poolSize)
    val readWriteLock: ReentrantReadWriteLock = new ReentrantReadWriteLock

    val inboundPacketsQueue: ConcurrentLinkedQueue[() => Unit] = new ConcurrentLinkedQueue[() => Unit]



    for(_<-0 until poolSize){
        pool.execute(this)
    }


    def addPacketToProcess(packet: () => Unit): Unit = {
        inboundPacketsQueue.add(packet)
    }

    override def run(): Unit = {

        while (server.isActive){
            var task:() => Unit = null
            readWriteLock.readLock.lock()
            try {
                if(!inboundPacketsQueue.isEmpty){
                    task = inboundPacketsQueue.poll()
                }
            } finally{
                readWriteLock.readLock.unlock()
            }
            if(task!=null){
                task()
            }else{
                Thread.sleep(1)
            }




        }

    }
}
