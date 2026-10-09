"""
==========================
ONION ARCHITECTURE IN PYTHON
==========================

---------------------------------------------------
WHAT IS ONION ARCHITECTURE?
---------------------------------------------------
Onion Architecture is a way to organize code in layers so that:
1. The core business logic stays **independent** from frameworks, databases, or UI.
2. The application becomes easier to maintain, test, and change.
3. Each layer handles a **single responsibility**.

---------------------------------------------------
LAYERS (from inner to outer):
---------------------------------------------------
1. **Domain Layer (Core)**
   - Contains Entities (data models) and Interfaces (contracts).
   - Contains only pure business logic — no database or framework code.

2. **Infrastructure Layer**
   - Implements repository interfaces.
   - Handles database operations (in this example, a fake in-memory DB is used).

3. **Service Layer (Application Layer)**
   - Contains business rules and orchestrates operations.
   - Calls repositories for data access but avoids direct dependency on database details.

4. **Presentation Layer**
   - Represents UI or API endpoints.
   - Receives user input and delegates work to the service layer.

---------------------------------------------------
FLOW OF THE PROCESS:
---------------------------------------------------
User/Frontend → Controller (Presentation Layer)
   → Service (Service Layer)
      → Repository (Infrastructure Layer)
         → Database (Fake DB here)

---------------------------------------------------
WHY THIS STRUCTURE WORKS WELL:
---------------------------------------------------
- **Dependency Inversion Principle (D in SOLID)** is maintained:
  High-level services depend on abstractions, not concrete repository implementations.
- **Single Responsibility Principle (S in SOLID)** is applied:
  Each layer does one job and does not mix concerns.
- **Open/Closed Principle (O in SOLID)** is supported:
  New features can be added without modifying core business logic.


This example demonstrates Onion Architecture using three entities:
- Students
- Courses
- Trainers

It follows the layered approach:
Domain (Core) → Infrastructure (Repositories) → Service (Business Logic) → Presentation (Controllers)
"""

# ----------------------------------------
# FAKE DATABASE
# ----------------------------------------
class Database:
    """
    Very simple in-memory database.
    Could be replaced by MySQL/PostgreSQL or any real DB without affecting business logic.
    """
    def __init__(self):
        self.students = []   # Holds student objects
        self.courses = []    # Holds course objects
        self.trainers = []   # Holds trainer objects


# ----------------------------------------
# DOMAIN LAYER (Core)
# ----------------------------------------

# --- Entities ---
class Student:
    """Entity representing a student."""
    def __init__(self, student_id, name, email):
        self.student_id = student_id
        self.name = name
        self.email = email

class Course:
    """Entity representing a course."""
    def __init__(self, course_id, title, trainer_id):
        self.course_id = course_id
        self.title = title
        self.trainer_id = trainer_id  # Link to trainer

class Trainer:
    """Entity representing a trainer."""
    def __init__(self, trainer_id, name, expertise):
        self.trainer_id = trainer_id
        self.name = name
        self.expertise = expertise


# --- Interfaces (Abstractions) ---
from abc import ABC, abstractmethod

# Student Repository Interface
class IStudentRepository(ABC):
    """
    Contract for student data access.
    Maintains Dependency Inversion by allowing services to depend on abstraction.
    """
    @abstractmethod
    def add_student(self, student): pass
    @abstractmethod
    def remove_student(self, student): pass
    @abstractmethod
    def update_student(self, student): pass
    @abstractmethod
    def get_all_students(self): pass

# Course Repository Interface
class ICourseRepository(ABC):
    """Contract for course data access."""
    @abstractmethod
    def add_course(self, course): pass
    @abstractmethod
    def remove_course(self, course): pass
    @abstractmethod
    def update_course(self, course): pass
    @abstractmethod
    def get_all_courses(self) -> list[Course]: pass

# Trainer Repository Interface
class ITrainerRepository(ABC):
    """Contract for trainer data access."""
    @abstractmethod
    def add_trainer(self, trainer): pass
    @abstractmethod
    def remove_trainer(self, trainer): pass
    @abstractmethod
    def update_trainer(self, trainer): pass
    @abstractmethod
    def get_all_trainers(self) -> list[Trainer]: pass


# Student Service Interface
class IStudentService(ABC):
    """Contract for student-related business logic."""
    @abstractmethod
    def add_student(self, student): pass
    @abstractmethod
    def remove_student(self, student): pass
    @abstractmethod
    def update_student(self, student): pass
    @abstractmethod
    def get_all_students(self): pass

# Course Service Interface
class ICourseService(ABC):
    """Contract for course-related business logic."""
    @abstractmethod
    def add_course(self, course): pass
    @abstractmethod
    def remove_course(self, course): pass
    @abstractmethod
    def update_course(self, course): pass
    @abstractmethod
    def get_all_courses(self) -> list[Course]: pass

# Trainer Service Interface
class ITrainerService(ABC):
    """Contract for trainer-related business logic."""
    @abstractmethod
    def add_trainer(self, trainer): pass
    @abstractmethod
    def remove_trainer(self, trainer): pass
    @abstractmethod
    def update_trainer(self, trainer): pass
    @abstractmethod
    def get_all_trainers(self) -> list[Trainer]: pass


# ----------------------------------------
# INFRASTRUCTURE LAYER (Repositories)
# ----------------------------------------

# Concrete Student Repository
class StudentRepository(IStudentRepository):
    """Implements student data access using in-memory DB."""
    def __init__(self, db):
        self.db = db

    def add_student(self, student):
        print("[Repository] Adding student to database")
        self.db.students.append(student)

    def remove_student(self, student):
        print("[Repository] Removing student from database")
        self.db.students.remove(student)

    def update_student(self, student):
        print("[Repository] Updating student in database (demo only)")
        return student

    def get_all_students(self):
        print("[Repository] Fetching all students from database")
        return self.db.students

# Concrete Course Repository
class CourseRepository(ICourseRepository):
    """Implements course data access using in-memory DB."""
    def __init__(self, db):
        self.db = db

    def add_course(self, course):
        print("[Repository] Adding course to database")
        self.db.courses.append(course)

    def remove_course(self, course):
        print("[Repository] Removing course from database")
        self.db.courses.remove(course)

    def update_course(self, course):
        print("[Repository] Updating course in database (demo only)")
        return course

    def get_all_courses(self) -> list[Course]:
        print("[Repository] Fetching all courses from database")
        return self.db.courses

# Concrete Trainer Repository
class TrainerRepository(ITrainerRepository):
    """Implements trainer data access using in-memory DB."""
    def __init__(self, db):
        self.db = db

    def add_trainer(self, trainer):
        print("[Repository] Adding trainer to database")
        self.db.trainers.append(trainer)

    def remove_trainer(self, trainer):
        print("[Repository] Removing trainer from database")
        self.db.trainers.remove(trainer)

    def update_trainer(self, trainer):
        print("[Repository] Updating trainer in database (demo only)")
        return trainer

    def get_all_trainers(self) -> list[Trainer]:
        print("[Repository] Fetching all trainers from database")
        return self.db.trainers


# ----------------------------------------
# SERVICE LAYER (Business Logic)
# ----------------------------------------

class StudentService(IStudentService):
    """Contains business rules and validation for students."""
    def __init__(self, student_repository: IStudentRepository):
        self.student_repository = student_repository

    def add_student(self, student):
        print("[Service] Checking if student can be added")
        # Prevent duplicate student IDs
        for existing in (self.student_repository.get_all_students() or []):
            if existing.student_id == student.student_id:
                print("[Service] Error: Student ID already exists.")
                return
        self.student_repository.add_student(student)
        print("[Service] Student added successfully.")

    def remove_student(self, student):
        print("[Service] Removing student")
        self.student_repository.remove_student(student)

    def update_student(self, student):
        print("[Service] Updating student")
        return self.student_repository.update_student(student)

    def get_all_students(self):
        print("[Service] Retrieving all students")
        return self.student_repository.get_all_students()

class CourseService(ICourseService):
    """Contains business rules for courses."""
    def __init__(self, course_repository: ICourseRepository):
        self.course_repository = course_repository

    def add_course(self, course):
        print("[Service] Adding course")
        self.course_repository.add_course(course)

    def remove_course(self, course):
        print("[Service] Removing course")
        self.course_repository.remove_course(course)

    def update_course(self, course):
        print("[Service] Updating course")
        return self.course_repository.update_course(course)

    def get_all_courses(self) -> list[Course]:
        print("[Service] Retrieving all courses")
        return self.course_repository.get_all_courses()

class TrainerService(ITrainerService):
    """Contains business rules for trainers."""
    def __init__(self, trainer_repository: ITrainerRepository):
        self.trainer_repository = trainer_repository

    def add_trainer(self, trainer):
        print("[Service] Adding trainer")
        self.trainer_repository.add_trainer(trainer)

    def remove_trainer(self, trainer):
        print("[Service] Removing trainer")
        self.trainer_repository.remove_trainer(trainer)

    def update_trainer(self, trainer):
        print("[Service] Updating trainer")
        return self.trainer_repository.update_trainer(trainer)

    def get_all_trainers(self) -> list[Trainer]:
        print("[Service] Retrieving all trainers")
        return self.trainer_repository.get_all_trainers()


# ----------------------------------------
# PRESENTATION LAYER (Controllers)
# ----------------------------------------

class StudentController:
    """Bridge between user input and student service."""
    def __init__(self, student_service: IStudentService):
        self.student_service = student_service

    def add_student(self, student):
        print("[Controller] Request received to add student")
        self.student_service.add_student(student)

    def remove_student(self, student):
        print("[Controller] Request received to remove student")
        self.student_service.remove_student(student)

    def update_student(self, student):
        print("[Controller] Request received to update student")
        return self.student_service.update_student(student)

    def get_all_students(self):
        print("[Controller] Request received to list students")
        return self.student_service.get_all_students()

class CourseController:
    """Bridge between user input and course service."""
    def __init__(self, course_service: ICourseService):
        self.course_service = course_service

    def add_course(self, course):
        print("[Controller] Request received to add course")
        self.course_service.add_course(course)

    def remove_course(self, course):
        print("[Controller] Request received to remove course")
        self.course_service.remove_course(course)

    def update_course(self, course):
        print("[Controller] Request received to update course")
        return self.course_service.update_course(course)

    def get_all_courses(self) -> list[Course]:
        print("[Controller] Request received to list courses")
        return self.course_service.get_all_courses()

class TrainerController:
    """Bridge between user input and trainer service."""
    def __init__(self, trainer_service: ITrainerService):
        self.trainer_service = trainer_service

    def add_trainer(self, trainer):
        print("[Controller] Request received to add trainer")
        self.trainer_service.add_trainer(trainer)

    def remove_trainer(self, trainer):
        print("[Controller] Request received to remove trainer")
        self.trainer_service.remove_trainer(trainer)

    def update_trainer(self, trainer):
        print("[Controller] Request received to update trainer")
        return self.trainer_service.update_trainer(trainer)

    def get_all_trainers(self) -> list[Trainer]:
        print("[Controller] Request received to list trainers")
        return self.trainer_service.get_all_trainers()


# ----------------------------------------
# MAIN PROGRAM (Dependency Injection + Execution)
# ----------------------------------------
if __name__ == "__main__":
    # Setting up dependencies
    db = Database()
    student_repo = StudentRepository(db)
    course_repo = CourseRepository(db)
    trainer_repo = TrainerRepository(db)

    student_service = StudentService(student_repo)
    course_service = CourseService(course_repo)
    trainer_service = TrainerService(trainer_repo)

    student_controller = StudentController(student_service)
    course_controller = CourseController(course_service)
    trainer_controller = TrainerController(trainer_service)

    # Creating trainers
    trainer1 = Trainer(1, "Mondol", "Python")
    trainer2 = Trainer(2, "Ali", "Data Science")
    trainer_controller.add_trainer(trainer1)
    trainer_controller.add_trainer(trainer2)

    # Creating courses
    course1 = Course(1, "Python Basics", trainer1.trainer_id)
    course2 = Course(2, "Machine Learning", trainer2.trainer_id)
    course_controller.add_course(course1)
    course_controller.add_course(course2)

    # Creating students
    student1 = Student(1, "Rashed", "rashed@example.com")
    student2 = Student(2, "Shuvo", "shuvo@example.com")
    student3 = Student(1, "DuplicateRashed", "duplicate@example.com")  # Duplicate ID

    student_controller.add_student(student1)
    student_controller.add_student(student2)
    student_controller.add_student(student3)  # Duplicate check triggers

    # Display all Students
    print("\n--- Students in Database ---")
    for s in (student_controller.get_all_students() or []):
        print("ID:", s.student_id, "| Name:", s.name, "| Email:", s.email)

    # Display all Trainers
    print("\n--- Trainers in Database ---")
    for t in trainer_controller.get_all_trainers():
        print("ID:", t.trainer_id, "| Name:", t.name, "| Expertise:", t.expertise)

    # Display all Courses
    print("\n--- Courses in Database ---")
    for c in course_controller.get_all_courses():
        print("ID:", c.course_id, "| Title:", c.title, "| Trainer ID:", c.trainer_id)


# output
# [Controller] Request received to add trainer
# [Service] Adding trainer
# [Repository] Adding trainer to database
# [Controller] Request received to add trainer
# [Service] Adding trainer
# [Repository] Adding trainer to database
# [Controller] Request received to add course
# [Service] Adding course
# [Repository] Adding course to database
# [Controller] Request received to add course
# [Service] Adding course
# [Repository] Adding course to database
# [Controller] Request received to add student
# [Service] Checking if student can be added
# [Repository] Fetching all students from database
# [Repository] Adding student to database
# [Service] Student added successfully.
# [Controller] Request received to add student
# [Service] Checking if student can be added
# [Repository] Fetching all students from database
# [Repository] Adding student to database
# [Service] Student added successfully.
# [Controller] Request received to add student
# [Service] Checking if student can be added
# [Repository] Fetching all students from database
# [Service] Error: Student ID already exists.

# --- Students in Database ---
# [Controller] Request received to list students
# [Service] Retrieving all students
# [Repository] Fetching all students from database
# ID: 1 | Name: Rashed | Email: rashed@example.com
# ID: 2 | Name: Shuvo | Email: shuvo@example.com

# --- Trainers in Database ---
# [Controller] Request received to list trainers
# [Service] Retrieving all trainers
# [Repository] Fetching all trainers from database
# ID: 1 | Name: Mondol | Expertise: Python
# ID: 2 | Name: Ali | Expertise: Data Science

# --- Courses in Database ---
# [Controller] Request received to list courses
# [Service] Retrieving all courses
# [Repository] Fetching all courses from database
# ID: 1 | Title: Python Basics | Trainer ID: 1
# ID: 2 | Title: Machine Learning | Trainer ID: 2