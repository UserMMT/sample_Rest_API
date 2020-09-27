# -*- coding: utf-8 -*-
"""
Created on Sat Sep 26 05:06:40 2020

@author: hp
"""
# you can call the API with 
# http://localhost:5000/language
# http://localhost:5000/language/null  ||http://localhost:5000/language/Java
from urllib.request import urlopen
from datetime import datetime, timedelta
from urllib.error import URLError, HTTPError
import json 
import flask
from flask import jsonify


global Json_error
Json_error={'error':'ressource not available'}

def send_all_languages():
    d = datetime.today() - timedelta(days=30)
    d=d.strftime("%Y-%m-%d")
    #print(d)

    url='https://api.github.com/search/repositories?q=created:<'+d+'&sort=stars&order=desc&page=1&per_page=100'
    print ('URL:',url)
    try:
        data=urlopen(url)
    except HTTPError as e:
    # do something
        print('Error code: ', e.code)
        return Json_error
    except URLError as e:
    # do something
        print('Reason: ', e.reason)
        return Json_error
# else:
#     # do something
#     print('good!')

    parsedata=json.loads(data.read())
    #print(parsedata)
    languages=[]
    lang=[]
    compteur=1
#query on total_count
    for datap in parsedata['items']:
        if len(languages) == 0:
        #first item
            languages.append({'name': datap['language'],'nbcount': 1,'ordre_tendance':'1','items':[datap]})
            lang.append(datap['language'])
            compteur+=1
            continue
        if datap['language'] in lang:
            for l in languages:
                if(datap['language']==l['name']):
                    l['nbcount']+=1
                    l['ordre_tendance']+=","+str(compteur)
                    l['items'].append(datap)
                    break;
            compteur+=1
            continue
        else:
            languages.append({'name': datap['language'],'nbcount': 1,'ordre_tendance':str(compteur),'items':[datap]})
            lang.append(datap['language'])
            compteur+=1
  
  
 
    print(lang)
  
    print("\n \n")
    for lp in languages:
    # code...
        print("\n languages "+str(lp['name']))
        print("Tendance "+lp['ordre_tendance'])
        print(" Nb count "+str(len(lp['items'])))
        print("Nombre count "+str(lp['nbcount']))
    # output=json.dumps(languages)
    # with open('all_language.json', 'w') as f:
    #     print(output,file=f)
    return languages
    
    
    
def send_specific_language(arg):
    d = datetime.today() - timedelta(days=30)
    d=d.strftime("%Y-%m-%d")
    #print(d)
    if arg=="null": #to call the null  langage repo
        arg=None
    
    url='https://api.github.com/search/repositories?q=created:<'+d+'&sort=stars&order=desc&page=1&per_page=100'
    print ('URL:',url)
    try:
        data=urlopen(url)
    except HTTPError as e:
    # do something
        print('Error code: ', e.code)
        return Json_error
    except URLError as e:
    # do something
        print('Reason: ', e.reason)
        return Json_error
# else:
#     # do something
#     print('good!')

    parsedata=json.loads(data.read())
    compteur=1
    language={'name': arg,'nbcount': 0,'ordre_tendance':'','items':[]}
            
    for datap in parsedata['items']:
        if(datap['language']==arg):
    
            if(language['nbcount']==0):
                language['nbcount']+=1;
                language['ordre_tendance']=str(compteur)
                language['items'].append(datap)
            else:
        
                language['nbcount']+=1;
                language['ordre_tendance']+=","+str(compteur)
                language['items'].append(datap)
        compteur+=1
        # code...
    print("\n languages "+str(language['name']))
    print("Tendance "+language['ordre_tendance'])
    print(" Nb count "+str(len(language['items'])))
    print("Nombre count "+str(language['nbcount']))
    # output=json.dumps(language)
    # with open('language_abc.json', 'w') as f:
    #     print(output,file=f)
    return language

app = flask.Flask(__name__)
app.config["DEBUG"] = True

@app.route('/', methods=['GET'])
def home():
    return '''<h1>Distant Reading Archive</h1>
<p>A prototype API for distant reading of science fiction novels.</p>'''


@app.route('/language', methods=['GET'])
def api_all():
    return jsonify(send_all_languages())


@app.route('/language/<LV>', methods=['GET'])
def api_id(LV):
    # Check if an ID was provided as part of the URL.
    # If ID is provided, assign it to a variable.
    # If no ID is provided, display an error in the browser.
    print(LV)
    
    return jsonify(send_specific_language(LV))

app.run()
    
# send_all_languages();

# send_specific_language("Java")
