pipeline {
	agent any
	
	tools{
		maven 'maven-3.9.9'
	}
	
	environment {
		COMPOSE_PATH = "${WORKSPACE}\\docker"
		SELEINUM_GRID_LOCAL = "true"
		SELEINUM_GRID_DOCKER = "true"
	}
	
	stages{
		stage('Start Selenium Grid via Docker Compose'){
			steps {
				script {
					echo "Starting selenium drid with docker compose"
					bat "docker compose -f ${COMPOSE_PATH}\\docker-compose.yml up -d"
					echo "waiting fro selenium grid to be ready..."
					sleep 120
				}
			}
		}
			
		stage('Checkout'){
			steps{
				git branch: 'main', url: 'https://github.com/Amathew5/Selenium-TestNG-Framework.git'
			}
		}
		
		stage('Build and Test'){
			steps{
				bat 'mvn clean install -DseleniumGrid=true'
			}
		}
		
		stage('Stop seleniumgrid'){
			steps{
				script{
					echo "Stopping selenium grid"
					bat "docker compose -f ${COMPOSE_PATH}\\docker-compose.yml down"
				}
			}
		}
		
		stage('Reports'){
			steps{
				publishHTML(target:[
					reportDir: 'src/test/resources/ExtentReport',
					reportFiles: 'ExtentReport.html',
					reportName: 'HTML Extent Report'
				])
			}
		}
	}
	
	post{
		always{
			archiveArtifacts artifacts: '**/src/test/resources/ExtentReport/*.html', fingerprint: true
			junit 'target/surefire-reports/*.xml'
		}
		
		success{
			emailext(
				to: 'anilkochu91@gmail.com',
				subject: "Built Success: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
				body:"""
				<html>
				<body>
				<p>The lastest Jenkins Build has completed.</p>
				<p><b>Project Name:</b> ${env.JOB_NAME}<bp>
				<p><b>Build Number:</b> ${env.BUILD_NUMBER}<bp>
				<p><b>Build Status:</b> <span style=color:green;"><b>SUCCESS</b>span</p>
				<p><b>Build URL:</b> <a href="${env.BUILD_URL}">${env.BUILD_URL}</a></p>
				
				<p><b>Last Commit:</b></p>
				<p>${env.GIT_COMMIT}</p>
				<p><b>Branch:</b> ${env.BIT_BRANCH}</p>
				
				<p><b>Build log is attached.</b></p>
				<p><b>Extent Report:</b> <a href="http://localhost://8080/job/OrangeHRM_BUILD/HTML_20Extent_20Report/">Click here</a></p>
				<p>Best regards,</p>
				<p><b>Automation Team</b></p> 
				</body>
				</html>
				""",
				mimeType: 'text/html',
				attachLog: true
			)
		}
	
		failure{
			emailext(
				to: 'anilkochu91@gmail.com',
				subject: "Built Success: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
				body:"""
				<html>
				<body>
				<p>The lastest Jenkins Build has <b style="color: red">FAILED</b>.</p>
				<p><b>Project Name:</b> ${env.JOB_NAME}<bp>
				<p><b>Build Number:</b> ${env.BUILD_NUMBER}<bp>
				<p><b>Build Status:</b> <span style=color:red;"><b>FAILED</b>span</p>
				<p><b>Build URL:</b> <a href="${env.BUILD_URL}">${env.BUILD_URL}</a></p>
				
				<p><b>Last Commit:</b></p>
				<p>${env.GIT_COMMIT}</p>
				<p><b>Branch:</b> ${env.BIT_BRANCH}</p>
				
				<p><b>Build log is attached.</b></p>
				<p><b>Extent Report:</b> <a href="http://localhost://8080/job/OrangeHRM_BUILD/HTML_20Extent_20Report/">Click here</a></p>
				<p>Best regards,</p>
				<p><b>Automation Team</b></p> 
				</body>
				</html>
				""",
				mimeType: 'text/html',
				attachLog: true
			)
		}
	}
}