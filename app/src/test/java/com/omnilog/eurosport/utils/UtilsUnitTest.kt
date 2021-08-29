package com.omnilog.eurosport.utils

import junit.framework.Assert.assertEquals
import org.junit.Test

class UtilsUnitTest {

    @Test
    fun floatToddMMMMyyyyDateIsCorrectFormat(){
        val dateStr = 1630136737000f.toddMMMMyyyyDate()
        assertEquals("28 August 2021", dateStr)
    }

    @Test
    fun given2ListsOfItemReturnOneListOfMixedOneByOne(){
        val list1 = listOf("1", "2", "3", "4", "5")
        val list2 = listOf("a", "b", "c")

        val mixList = list1.mix(list2)

        val expectedMixList = listOf("1", "a", "2", "b", "3", "c", "4", "5")
        assertEquals(expectedMixList, mixList)
    }
}