//
//  ViewController.swift
//  SaveTheFoodIOS
//
//  Created by Andrea Franco on 2021-08-29.
//

import UIKit
import shared

class ViewController: UIViewController {

    // TODO add Koin like https://www.futuremind.com/blog/handling-kotlin-multiplatform-coroutines-koru
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        let db = DatabaseFactory(databaseDriverFactory: DatabaseDriverFactory()).createDatabase()
        
        let vm = LoginViewModel(userDataRepository: UserDataRepository(userLocalDataSource: UserLocalDataSource(foodDatabase: db), userRemoteDataSource: UserRemoteDataSource()))
        
        print(vm.test.value)
    }


}

