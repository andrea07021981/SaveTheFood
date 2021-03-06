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
                echo 'Preparation Dev'
                withCredentials([file(credentialsId: 'googleservicesdev', variable: 'googleservicesdev')]) {
                    // command for jenkins localhost
                    sh "cp \$googleservicesdev /Users/andreafranco/.jenkins/workspace/SaveTheFoodMasterPipeline_master/app/src/dev/google-services.json"

                    /* command for jenkins docker
                    sh "cp \$googleservicesdev /var/jenkins_home/workspace/SaveTheFood_master/app/src/dev/google-services.json"
                    +/
                }
                withCredentials([file(credentialsId: 'MAPSAPI', variable: 'MAPSAPI')]) {
                    // command for jenkins localhost
                    sh "cp \$MAPSAPI /Users/andreafranco/.jenkins/workspace/SaveTheFoodMasterPipeline_master/app/src/dev/res/values/google_maps_api.xml"

                    /* command for jenkins docker
                    sh "cp \$MAPSAPI /var/jenkins_home/workspace/SaveTheFood_master/app/src/dev/res/values/google_maps_api.xml"
                    */
                }

                echo 'Preparation Uat'
                withCredentials([file(credentialsId: 'googleservicesuat', variable: 'googleservicesuat')]) {
                    // command for jenkins localhost
                    sh "cp \$googleservicesuat /Users/andreafranco/.jenkins/workspace/SaveTheFoodMasterPipeline_master/app/src/uat/google-services.json"

                    /* command for jenkins docker
                    sh "cp \$googleservicesuat /var/jenkins_home/workspace/SaveTheFood_master/app/src/uat/google-services.json"
                    */

                }
                withCredentials([file(credentialsId: 'MAPSAPI', variable: 'MAPSAPI')]) {
                    // command for jenkins localhost
                    sh "cp \$MAPSAPI /Users/andreafranco/.jenkins/workspace/SaveTheFoodMasterPipeline_master/app/src/uat/res/values/google_maps_api.xml"

                    /* command for jenkins docker
                    sh "cp \$MAPSAPI /var/jenkins_home/workspace/SaveTheFood_master/app/src/uat/res/values/google_maps_api.xml"
                    */
                }

                echo 'Preparation Prod'
                withCredentials([file(credentialsId: 'googleservices', variable: 'googleservices')]) {
                    // command for jenkins localhost
                    sh "cp \$googleservices /Users/andreafranco/.jenkins/workspace/SaveTheFoodMasterPipeline_master/app/src/prod/google-services.json"

                    /* command for jenkins docker
                    sh "cp \$googleservices /var/jenkins_home/workspace/SaveTheFood_master/app/src/prod/google-services.json"
                    */
                }
                withCredentials([file(credentialsId: 'MAPSAPI', variable: 'MAPSAPI')]) {
                    // command for jenkins localhost
                    sh "cp \$MAPSAPI /Users/andreafranco/.jenkins/workspace/SaveTheFoodMasterPipeline_master/app/src/prod/res/values/google_maps_api.xml"

                    /* command for jenkins docker
                    sh "cp \$MAPSAPI /var/jenkins_home/workspace/SaveTheFood_master/app/src/prod/res/values/google_maps_api.xml"
                    */
                }

            }
        }
        stage('Lint App') {
            steps {
                echo 'Running Lint'
                sh 'chmod +x gradlew'
                sh './gradlew lint'
            }
        }
        stage('Build App') {
            steps {
                echo 'Running Build'
                sh 'chmod +x gradlew'
                sh './gradlew clean'
                sh './gradlew build -x test'
            }
        }
        stage('Assemble Dev') {
            steps {
                echo 'Running Assemble Dev'
                sh 'chmod +x gradlew'
                sh './gradlew assembleDev'
            }
        }

        stage('Assemble Uat') {
            steps {
                echo 'Running Assemble Uat'
                sh 'chmod +x gradlew'
                sh './gradlew assembleUat'
            }
        }

        stage('Assemble Prod') {
            steps {
                echo 'Running Assemble Prod'
                sh 'chmod +x gradlew'
                sh './gradlew assembleProd'
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
        stage('Deploy') {
            steps {
                echo 'Deploy Release Build'
            }
        }
    }
}
