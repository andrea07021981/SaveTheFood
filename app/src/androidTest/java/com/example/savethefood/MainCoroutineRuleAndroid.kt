package com.example.savethefood

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

//This swap the standard coroutine main dispatcher to the test dispatcher, better performance
@ExperimentalCoroutinesApi
class MainCoroutineRuleAndroid(private val dispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()):
    TestWatcher(),//this makes MainCoroutineRule a JUnit Rule
    TestCoroutineScope by TestCoroutineScope(dispatcher) {
    override fun starting(description: Description?) {
        super.starting(description)
        Dispatchers.setMain(dispatcher)
    }

    override fun finished(description: Description?) {
        super.finished(description)
        cleanupTestCoroutines()
        Dispatchers.resetMain()
    }
}