pipeline {
  agent any
  environment { JENKINS_NODE_COOKIE = 'dontKillMe' }
  stages {
    stage('Checkout') { steps { checkout scm } }
    stage('Test')     { steps { bat 'mvn clean test' } }
    stage('Package')  { steps { bat 'mvn package -DskipTests' } }
    stage('Deploy') {
      steps {
        bat '''
        powershell -Command "Get-NetTCPConnection -LocalPort 8081 -ErrorAction SilentlyContinue | ForEach-Object { Stop-Process -Id $_.OwningProcess -Force }"
        if not exist C:\\myapp mkdir C:\\myapp
        copy /Y target\\library-0.0.1-SNAPSHOT.jar C:\\myapp\\app.jar
        powershell -Command "Start-Process java -ArgumentList '-jar','C:\\myapp\\app.jar' -WindowStyle Hidden"
        '''
      }
    }
  }
  post { always { junit 'target/surefire-reports/*.xml' } }
}