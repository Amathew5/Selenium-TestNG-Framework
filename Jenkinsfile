pipeline {
	agent any
	
	tools{
		maven 'maven-3.9.9'
	}
	
	stages{
		stage('Checkout'){
			steps{
				git branch: 'main', url: 'https://github.com/Amathew5/Selenium-TestNG-Framework.git'
			}
		}
		
		stage('Build'){
			steps{
				bat 'mvn clean install'
			}
		}
		
		stage('Test'){
			steps{
				bat 'mvn test'
			}
		}
		
		stage('Reports'){
			steps{
				publishHTML(target:[
					reportDir: 'src/test/resources/ExtentReport',
					reportFiles: 'ExtentReport.html',
					reportnAME: 'Extent Report'
				])
			}
		}
	}
	
	post{
		always{
			archiveArtifacts artifacts: '**/src/test/resources/ExtentReport/*.html', fingerprint: true
			junit 'target/surefire-reports/8.xml'
		}
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
			mimrtype: 'text/html',
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
			mimrtype: 'text/html',
			attachLog: true
		)
	}
}