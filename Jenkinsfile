pipeline {
    agent any
    stages {
        stage('Build') {
            environment{
                APPIAN_CLASSPATH = '/usr/local/appian/ear/suite.ear/lib'
            }
			steps {
            	echo "Running ${env.BUILD_ID} on ${env.JENKINS_URL}"
                echo 'Building..'
                sh 'ant'
          		archiveArtifacts artifacts: '*.jar', fingerprint: true
            }
        }
        stage('Test') {
            steps {
                echo 'Test stage..Nothing to do'
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploy stage....Nothing to do'
            }
        }
    }
}