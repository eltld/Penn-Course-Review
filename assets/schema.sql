DROP TABLE IF EXISTS Department
DROP TABLE IF EXISTS Instructor
DROP TABLE IF EXISTS Course
DROP TABLE IF EXISTS Code
DROP TABLE IF EXISTS CourseCode
DROP TABLE IF EXISTS Semester
DROP TABLE IF EXISTS CourseDetail
DROP TABLE IF EXISTS CourseSection
DROP TABLE IF EXISTS Recent
DROP TABLE IF EXISTS InvertedIndex

CREATE TABLE Department (dept_id TEXT NOT NULL PRIMARY KEY, dept_name TEXT NOT NULL, dept_viewd INTEGER NOT NULL)
CREATE TABLE Instructor (instr_id TEXT NOT NULL PRIMARY KEY, instr_lastName TEXT NOT NULL, instr_firstName TEXT NOT NULL, instr_viewed INTEGER NOT NULL)
CREATE TABLE Course (course_id TEXT NOT NULL PRIMARY KEY, course_name TEXT NOT NULL, course_viewed INTEGER NOT NULL )
CREATE TABLE Code (code_code TEXT NOT NULL PRIMARY KEY, code_deptId TEXT NOT NULL, FOREIGN KEY (code_deptId) REFERENCES Department (dept_id))
CREATE TABLE CourseCode (cc_codeId TEXT NOT NULL, cc_courseId TEXT NOT NULL, PRIMARY KEY (cc_codeId, cc_courseId), FOREIGN KEY (cc_courseId) REFERENCES Course (course_id), FOREIGN KEY (cc_codeId) REFERENCES Code (code_code))
CREATE TABLE Semester (semester_id TEXT NOT NULL PRIMARY KEY, semester_name TEXT NOT NULL, semester_year INTEGER NOT NULL, semester_season TEXT NOT NULL)
CREATE TABLE CourseDetail (cd_id TEXT NOT NULL PRIMARY KEY, cd_courseId TEXT NOT NULL, cd_semesterId TEXT NOT NULL, cd_description TEXT NOT NULL, FOREIGN KEY (cd_courseId) REFERENCES Course (course_id), FOREIGN KEY (cd_semesterId) REFERENCES Semester (semester_id))
CREATE TABLE CourseSection (cs_courseDetailId TEXT NOT NULL, cs_primaryAlias TEXT NOT NULL, cs_section TEXT NOT NULL, cs_instrId TEXT NOT NULL, cs_ratings TEXT NOT NULL, FOREIGN KEY (cs_courseDetailId) REFERENCES CourseDetail (cd_id), FOREIGN KEY (cs_instrId) REFERENCES Instructor (instr_id), PRIMARY KEY (cs_courseDetailId, cs_section, cs_instrId))
CREATE TABLE InvertedIndex (ii_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, ii_keyword TEXT NOT NULL, ii_fullText TEXT NOT NULL, ii_type TEXT NOT NULL, ii_refId TEXT NOT NULL)
CREATE TABLE Recent (recent_fullText TEXT NOT NULL, recent_type TEXT NOT NULL, recent_refId TEXT NOT NULL, recent_timeStamp INTEGER NOT NULL, PRIMARY KEY (recent_fullText, recent_type, recent_refId))

CREATE INDEX instr_name ON Instructor (instr_lastName, instr_firstName)
CREATE INDEX code_deptId ON Code (code_deptId)
CREATE INDEX cd_courseId ON CourseDetail (cd_courseId)
CREATE INDEX cs_courseDetailId ON CourseSection (cs_courseDetailId)
CREATE INDEX cs_instrId ON CourseSection (cs_instrId)
CREATE INDEX ii_keyword ON InvertedIndex (ii_keyword)
CREATE INDEX recent_timeStamp ON Recent (recent_timeStamp)