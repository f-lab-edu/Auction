pipeline{

    agent any

    tools {
        gradle 'Gradle-7.1.1'
    }

    environment {
        AWS_S3_ACCESS_KEY = credentials('AWS_S3_ACCESS_KEY')
        AWS_S3_SECRET_KEY = credentials('AWS_S3_SECRET_KEY')
        AWS_S3_REGION_KEY = credentials('AWS_S3_REGION_KEY')
    }

    stages {

        stage("Build"){
            steps{
                sh 'chmod +x gradlew'
                sh './gradlew cleanQuerydslSourcesDir'
                sh './gradlew bootJar'
                echo '------ Run spring boot FINISH ------'
            }
        }

        stage('ImagePush'){
            steps{
                sh 'rm  ~/.dockercfg || true'
                sh 'rm ~/.docker/config.json || true'

                script{
                    docker.withRegistry('https://758812638430.dkr.ecr.ap-northeast-2.amazonaws.com/auction', 'ecr:ap-northeast-2:ecr-credentials'){
                           def image = docker.build("758812638430.dkr.ecr.ap-northeast-2.amazonaws.com/auction")
                           image.push("${env.BUILD_NUMBER}")
                           image.push('latest')
                    }
                echo '------Build and Push image FINISH ------'
                }
            }
        }

        stage('Deploy'){
            steps{
                script{
                       withAWS(credentials:"AWS_CREDENTIALS", region: 'ap-northeast-2') {
                             sh 'aws ecs update-service --region ap-northeast-2 --cluster auction --service auction --force-new-deployment'
                     }
                echo '------Deploy FINISH ------'
                }
            }
        }
    }

    post {
        failure {
                mail (
                        from: "ayrs02117a@gmail.com",
                        to: "ayrs02117a@gmail.com",
                        subject: "${env.JOB_NAME} Failure - BuildNumber:${BUILD_NUMBER}",
                        body: "${env.JOB_NAME} Failure - BuildNumber:${BUILD_NUMBER}"
                      )
         }
    }

}