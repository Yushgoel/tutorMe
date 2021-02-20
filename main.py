import flask
from flask import request
import os
import logging
import random
import pandas as pd
from google.cloud import storage
import io
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import linear_kernel

CLOUD_STORAGE_BUCKET = os.environ.get('CLOUD_STORAGE_BUCKET')
app = flask.Flask(__name__)

user_temp = pd.DataFrame()
questions = pd.DataFrame()

database = {
	# Calc Related Topics
	"calculus": 0,
	"integration": 0,
	"differentiation": 0,
	"minimizing": 0,

	# Polynomial, Equations Related Topics
	"polynomials": 1,
	"equations": 1,
	"inequality": 1
}


# columns for Users DataFrame:
# userID    |    name     |    email   |    role

# Columns for the Questions DataFrame
# questionID    |   userID    | 	 subject  |     grade   |    topic  |  desc 	 |   status   |   teacherID   |     rating

@app.route('/')
def root():
    return "Testing"

# def convertToJson(df):
	# json = {}
	# for index, row in df.iterrows():
	

@app.route('/AddUser')
def add_user():
	user_temp = read_users()
	userID = len(user_temp)
	userEmail = request.args['email']
	userName = request.args['name']
	userRole = request.args['role']
	user_temp.loc[len(user_temp)] = [userID, userName, userEmail, userRole] # Add user to the dataframe
	save_users(user_temp)
	return str(userID)


@app.route('/AddQuestion')
def add_question():
	question_temp = read_questions()
	userID = int(request.args['userID'])
	subject = request.args['subject']
	grade = request.args['grade']
	topic = request.args['topic']
	desc = request.args['desc']
	status = 0
	teacherID = -1
	
	questionID = len(question_temp) # generating index numbers for thee questions - this is for the recommender system - starts from 0 itself - to prevent condfuision between index and this
	
	question_temp.loc[len(question_temp)] = [questionID, userID, subject, grade, topic, desc, status, teacherID]
	save_questions(question_temp)
	return str(questionID)

def read_users(): 
	temp = pd.read_csv('https://storage.googleapis.com/tutor_app_data/users.csv')
	return temp
	# temp = pd.DataFrame([[0, "abc", "trial@gmail.com", "Student"]], columns=["userID", "name", "email", "role"])
	# return temp

def read_questions():
	temp = pd.read_csv('https://storage.googleapis.com/tutor_app_data/questions.csv')
	return temp
	# temp = pd.DataFrame([[0, 0, "Maths", "10th", "Calculus", "How Does the Power Rule Work", 0, -1]], columns=["questionID", "userID", "subject", "grade", "topic", "desc", "status", "teacherID"])
	# return temp


def save_users(users_datum): 
	gcs = storage.Client()
	bucket = gcs.get_bucket(CLOUD_STORAGE_BUCKET)

	users_file = 'users.csv'

	blob = bucket.blob(users_file)

	blob.cache_control = 'no-cache' #should we keep this
	s = io.StringIO()
	users_datum.to_csv(s, index=False)
	blob.upload_from_string(s.getvalue(), content_type='text/csv')


def save_questions(questions_datum): 
	gcs = storage.Client()
	bucket = gcs.get_bucket(CLOUD_STORAGE_BUCKET)

	questions_file = 'questions.csv'

	blob = bucket.blob(questions_file)

	blob.cache_control = 'no-cache' 
	s = io.StringIO()
	questions_datum.to_csv(s, index=False)
	blob.upload_from_string(s.getvalue(), content_type='text/csv')




@app.route('/GetQuestionsForStudent')
def get_for_student():
	userid = int(request.args['userid'])
	questions_temp = read_questions()
	questions_filtered  = questions_temp[questions_temp['userID'] == userid]
	return questions_filtered.to_dict('index')


@app.route('/GetQuestionsForTeacher')
def get_for_teacher():

	teacherid = int(request.args['teacherId'])
	
	questions = reccomend(teacherid)
	return questions.to_dict('index')
	# return "0"

def reccomend(teacherid):

	questions = read_questions()

	filtered_questions = questions[questions["teacherID"] == teacherid]

	filtered_topics = filtered_questions['desc'].values

	filtered_categories = []

	for topic in filtered_topics:
		split = topic.split()

		for word in split:
			if database.__contains__(word.lower()):
				filtered_categories.append(database[word])
				break
		
	
	unanswered_questions = questions[questions["status"] == 0]
	unanswered_topics = unanswered_questions['desc'].values

	unanswered_categories = []

	for topic in unanswered_topics:
		split = topic.split()

		for word in split:
			if database.__contains__(word.lower()):
				unanswered_categories.append(database[word])
				break
	
	rec_questions = []

	filtered_categories = remove_duplicates(filtered_categories)
	# unanswered_categories = remove_duplicates(unanswered_categories)

	for category in filtered_categories:
		for j in range(0, 2):
			i = 0
			for unanswered_category in unanswered_categories:
				if category == unanswered_category:
					break
				i += 1
			
			rec_questions.append(i)
			unanswered_categories.pop(i)


	while (len(rec_questions) < 6) and (len(rec_questions) != len(unanswered_questions)):
		index = random.randint(0, len(unanswered_questions) - 1)
		if index not in rec_questions:
			rec_questions.append(index)

	compiled_questions = unanswered_questions.iloc[rec_questions, :]	
	return compiled_questions


def remove_duplicates(array):
	return [array.append(x) for x in array if x not in array]


@app.route('/QuestionPicked')
def question_picked():
	temp = read_questions()
	questionID = int(request.args['questionID'])
	teacherID = int(request.args['teacherID'])
	print(temp)
	#filtered_questions = temp[temp['questionID'] == questionID]
	#filtered_questions
	temp['status'].where(~(temp['questionID'] == questionID), other=1, inplace=True)
	temp['teacherID'].where(~(temp['questionID'] == questionID), other=teacherID, inplace=True)
	save_questions(temp)
	return temp.to_dict('index')

@app.route('/QuestionFinished')
def question_answered():
	#rating has to be done here
	temp = read_questions()
	questionID = int(request.args['questionID'])
	temp['status'].where(~(temp['questionID'] == questionID), other=2, inplace=True)
	save_questions(temp)
	return temp.to_dict('index')

if __name__ == '__main__':
	app.run(host = '127.0.0.1', port=8080, debug=True)
