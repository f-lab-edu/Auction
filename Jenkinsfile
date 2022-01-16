pipeline{

    agent any

    tools {
        gradle 'Gradle-7.1.1'
    }

    stages {

        stage("Run spring boot"){
            steps{
                echo 'executing gradle.....'
                sh 'chmod +x gradlew'
                sh './gradlew cleanQuerydslSourcesDir'
                sh './gradlew bootJar'
            }
        }

        stage('Build and Push image'){
            steps{

                script{
                    docker.withRegistry('https://473778033566.dkr.ecr.us-east-1.amazonaws.com/auction-service:latest',
                                                'ecr:us-east-1:473778033566'){
                           def image = docker.build("auction-service")
                           image.push()
                    }
                }
            }
        }
    }
}