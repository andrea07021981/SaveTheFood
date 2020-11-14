pipeline {
    /*
        Agent is where this the script runs, in this case any available Jenkins agent.
        We can change it when we have clusters and master/slave with Docker
    */
    agent any

    triggers{ cron('H H(9-16)/2 * * 1-5') } // once in every two hours slot between 9 AM and 5 PM every weekday (perhaps at 10:38 AM, 12:38 PM, 2:38 PM, 4:38 PM)

    stages {
        stage('Preparation') {
            steps {
                echo 'Preparation'
                withCredentials([file(credentialsId: 'googleservices', variable: 'googleservices')]) {
                    sh "cp \$googleservices /Users/andreafranco/.jenkins/workspace/FirstSaveTheFoodBuild/app/google-services.json"
                }
                withCredentials([file(credentialsId: 'MAPSAPI', variable: 'MAPSAPI')]) {
                    sh "cp \$MAPSAPI /Users/andreafranco/.jenkins/workspace/FirstSaveTheFoodBuild/app/src/main/res/values/google_maps_api.xml"
                }
            }
        }
        stage('Build') {
            steps {
                echo 'Running Build'
                sh 'chmod +x gradlew'
                sh './gradlew clean'
                sh './gradlew build -x test'
            }
        }
        stage('Assemble') {
            steps {
                echo 'Running Build'
                sh 'chmod +x gradlew'
                sh './gradlew assembleDebug'
            }
        }
        stage('Unit Test') {
            steps {
                echo 'Unit Testing'
                sh 'chmod +x gradlew'
                sh './gradlew test'
            }
        }
        stage('Integration Test') {
            steps {
                echo 'Integration Testing'
            }
        }
        stage('UI Test') {
            steps {
                echo 'Unit Testing'
            }
        }
        stage('Release') {
            steps {
                echo 'Release Build'
                sh 'chmod +x gradlew'
                sh './gradlew assembleRelease'
            }
        }
        stage('Deploy') {
            steps {
                echo 'Running Build'
                sh 'chmod +x gradlew'
                sh './gradlew installRelease'
            }
        }
    }
}
