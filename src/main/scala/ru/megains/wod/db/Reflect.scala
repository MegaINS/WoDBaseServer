package ru.megains.wod.db

import scala.reflect.ClassTag

object Reflect {


    def createInstance[T: ClassTag](fqcn: String, classLoader: ClassLoader): T = {
        try {
            createInstance(getClass(fqcn, classLoader))
        } catch {
            case e: VirtualMachineError => throw e
            case e: ThreadDeath => throw e
            case e: Throwable =>
                val name = simpleName(implicitly[ClassTag[T]].runtimeClass)
                throw new Exception(s"Cannot load $name  $name [$fqcn] cannot be instantiated.", e)
        }
    }
    def createInstance[T: ClassTag](clazz: Class[_]): T = {
        val o = clazz.newInstance
        val t = implicitly[ClassTag[T]].runtimeClass
        if (t.isInstance(o)) o.asInstanceOf[T]
        else throw new ClassCastException(clazz.getName + " is not an instance of " + t)
    }

    def simpleName(clazz: Class[_]): String = {
        val name = clazz.getName
        name.substring(name.lastIndexOf('.') + 1)
    }

    def getClass[T: ClassTag](fqcn: String, classLoader: ClassLoader): Class[_ <: T] = {
        val c = Class.forName(fqcn, false, classLoader).asInstanceOf[Class[_ <: T]]
        val t = implicitly[ClassTag[T]].runtimeClass
        if (t.isAssignableFrom(c)) c
        else throw new ClassCastException(t + " is not assignable from " + c)
    }
}
