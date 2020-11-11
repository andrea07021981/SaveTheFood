pipeline {
    agent any
    
    stages {
        stage('Build') {
            steps {
                echo 'Running Build'
                chmod +x gradlew
                ./gradlew clean
                ./gradlew assembleRelease
            }
        }
        stage('Test') {
            steps {
                echo 'Testing'
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying'
            }
        }
    }
}
