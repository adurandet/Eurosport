package com.eurosport

import org.mockito.ArgumentCaptor
import org.mockito.Mockito

inline fun <reified T> mock(): T = Mockito.mock(T::class.java)
inline fun <reified T> forClass(): ArgumentCaptor<T> = ArgumentCaptor.forClass(T::class.java)