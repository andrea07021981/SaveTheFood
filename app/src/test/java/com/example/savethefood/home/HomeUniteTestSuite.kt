package com.example.savethefood.home

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.runner.RunWith
import org.junit.runners.Suite

// Runs all unit tests.
@ExperimentalCoroutinesApi
@RunWith(Suite::class)
@Suite.SuiteClasses(
    //HomeLocalDataSourceTest::class,
    //omeRemoteDataSourceTest::class,
    HomeRepositoryTest::class)
    //HomeViewModelTest::class)
class UnitTestSuite