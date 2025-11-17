# Hamid_2207067_GPA_CALCULATOR

JavaFX GPA Calculator application (Home → Course Entry → GPA Result) built with FXML and controllers.

Requirements:

- Java 17 or newer
- Maven

Run (PowerShell on Windows):

```powershell
cd C:\Users\USER\IdeaProjects\Hamid_2207067_GPA_CALCULATOR
mvn javafx:run
```

Notes:

- The project includes FXML layouts in `src/main/resources/com/example/gpa` and controllers in `src/main/java/com/example/gpa/controller`.
- Enter the total required credits in the Course Entry screen. The "Calculate GPA" button will enable when the sum of entered course credits equals that total.


## Project Structure
```

Hamid_2207067_GPA_CALCULATOR/
├─ .git/
├─ .gitignore
├─ .idea/
├─ .mvn/
├─ Image_of_project/
├─ mvnw
├─ mvnw.cmd
├─ pom.xml
├─ README.md
└─ src/
	└─ main/
		├─ java/
		│  └─ com/
		│     └─ example/
		│        └─ gpa/
		│           ├─ MainApp.java
		│           ├─ controller/
		│           │  ├─ CourseController.java
		│           │  ├─ HomeController.java
		│           │  └─ ResultController.java
		│           ├─ model/
		│           │  └─ Course.java
		│           └─ util/
		│              └─ Navigation.java
		└─ resources/
			└─ com/
				└─ example/
					└─ gpa/
						├─ home.fxml
						├─ course_entry.fxml
						├─ result.fxml
						└─ styles.css
```

# Hamid_2207067_GPA_CALCULATOR

Images of project: https://github.com/N-nashita/Hamid_2207067_GPA_CALCULATOR/tree/main/Image_of_project