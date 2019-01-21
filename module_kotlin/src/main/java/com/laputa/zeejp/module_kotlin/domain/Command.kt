package com.laputa.zeejp.module_kotlin.domain

public interface Command<T>{
    fun execute():T
}