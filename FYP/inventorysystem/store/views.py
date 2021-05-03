# Author By Koo Chung Hing

from django.shortcuts import render,redirect
from django.http import HttpResponseRedirect
import pyrebase
import pandas as pd
import json
import plotly.express as px
from plotly.offline import plot
from random import choices
from django.contrib import auth

#create config to Firebase
config = {

    'apiKey': "AIzaSyDrLlwYpuf7QcqBpBtA9mr3T-eeNxXPH2A",
    'authDomain': "fyps-1c8f1.firebaseapp.com",
    'databaseURL': "https://fyps-1c8f1-default-rtdb.firebaseio.com",
    'projectId': "fyps-1c8f1",
    'storageBucket': "fyps-1c8f1.appspot.com",
    'messagingSenderId': "535977098385",
    'appId': "1:535977098385:web:e3ac8c5e7844c5a8121330",
    'measurementId': "G-FCZJZVH9RG"
}
#allow the server to authenticate with Firebase as an admin and disregard any security rules
firebase = pyrebase.initialize_app(config)
#use multiple Firebase services - Authentication
auth = firebase.auth()
#use ultiple Firebase services - RealTimme Database
db = firebase.database()




def login(request):
    return render(request,'login.html')


def home(request):
    # Get the user login input
    username = request.POST.get("username")
    useremail = username+"@system.com"
    password = request.POST.get("password")
    try:
        # firebase login - Authentication
        user = auth.sign_in_with_email_and_password(useremail, password)
        # get the firebase realtime directory and use the pandas to change the database and make it have columns
        dftable2 = pd.DataFrame.from_dict(db.child("SystemUser").get().val(), orient="index").reset_index()
        # use the cache store the value
        request.session['userlevel'] = int(dftable2.loc[dftable2['Username'] == username]['UserLevel'])
        # set the username
        request.session['username'] = username
    except:
        messages="invalid credentials! Please Input Again!"
        return render(request, 'login.html',{"mes":messages})
    session_id = user['idToken']
    request.session['uid'] = str(session_id)
    return render(request,'home.html',{"userlevel":request.session['userlevel']})



def Customer(request):
    if request.method == 'POST':
        Balance = request.POST.get("raisebalance","")
        HideBalance = request.POST.get("hiddenbalance","")
        Email = request.POST.get("hiddenemail","")
        # find the value key
        Customerdicts = db.child("Users").order_by_child("email").equal_to(Email).get()
        # user the key to find the item and update
        for val in Customerdicts.each():
            db.child("Users").child(val.key()).update({'balance': (int(HideBalance)+int(Balance))})
        return HttpResponseRedirect('/product/')
    else:
        Email = request.GET.get('email')
        # get the firebase realtime directory and use the pandas to change the database and make it have columns
        Customerdict = db.child("Users").order_by_child("email").equal_to(Email).get().val()
        # Customer directory always have one
        for val in Customerdict.values():
            Customers = val
        return render(request,'Customer.html',dict(Customers))

def Store(request):
    # get the firebase realtime directory and use the pandas to change the database and make it have columns
    dftable = pd.DataFrame.from_dict(db.child("Stock").get().val(), orient="index").reset_index()
    # Data table change to directory
    json_records = dftable.reset_index().to_json(orient='records')
    data = []
    data = json.loads(json_records)
    context = {'d': data}
    context['userlevel'] = request.session['userlevel']
    return render(request, 'Store.html', context)

def Product(request):
    # get the firebase realtime directory and use the pandas to change the database and make it have columns
    dftable = pd.DataFrame.from_dict(db.child("Stock").get().val(), orient="index").reset_index()
    # Data table change to directory
    json_records = dftable.reset_index().to_json(orient='records')
    data = []
    data = json.loads(json_records)
    context = {'d': data}
    context['userlevel'] = request.session['userlevel']
    return render(request, 'Product.html', context)

def EditStock(request):
    if request.method == 'POST':
        Qty = request.POST.get("Qty","")
        id = request.POST.get('StockID',"")
        StockItem = db.child("Stock").order_by_child("StockID").equal_to(id).get()
        for val in StockItem.each():
            db.child("Stock").child(val.key()).update({'Qty': int(Qty)})
        return HttpResponseRedirect('/stock/')
    else:
        id = request.GET.get('id')
        stockdict = db.child("Stock").order_by_child("StockID").equal_to(id).get().val()
        for val in stockdict.values():
            stock = val
        return render(request, 'EditStock.html', dict(stock))

def EditProduct(request):
    if request.method == 'POST':
        Price = request.POST.get("Price","")
        id = request.POST.get('StockID',"")
        StockItem = db.child("Stock").order_by_child("StockID").equal_to(id).get()
        for val in StockItem.each():
            db.child("Stock").child(val.key()).update({'Price': int(Price)})
        return HttpResponseRedirect('/product/')
    else:
        id = request.GET.get('id')
        stockdict = db.child("Stock").order_by_child("StockID").equal_to(id).get().val()
        for val in stockdict.values():
            stock = val
        return render(request, 'EditProduct.html', dict(stock))

def balance(request):
    if request.method == 'POST':
        Email = request.POST.get("Custemail","")
        url = "../Customer/?email="+Email
        return HttpResponseRedirect(url)
    else:
        return render(request, 'Userblanace.html')

def SystemAccount(request):
    # get the firebase realtime directory and use the pandas to change the database and make it have columns
    dftable = pd.DataFrame.from_dict(db.child("SystemUser").get().val(), orient="index").reset_index()
    # Data table change to directory
    json_records = dftable.reset_index().to_json(orient='records')
    data = []
    data = json.loads(json_records)
    context = {'d': data}
    context['userlevel'] = request.session['userlevel']
    return render(request, 'SystemAccount.html', context)

def AddSystemAccount(request):
    if request.method == 'POST':
        Username = request.POST.get("Username","")
        Useremail = str(Username)+"@system.com"
        Password = request.POST.get("Password","")
        UserLevel = int(request.POST.get("UserLevel", ""))
        # firebase add the user - Authentication
        auth.create_user_with_email_and_password(Useremail,Password)
        systemuser = {"Username" : Username,"Password" : Password,"UserLevel" : UserLevel}
        db.child("SystemUser").push(systemuser)
        return HttpResponseRedirect('/SystemAccount/')
    else:
        return render(request, 'AddSystemAccount.html')

def Report(request):
    # get the firebase realtime directory and use the pandas to change the database and make it have columns
    dftable = pd.DataFrame.from_dict(db.child("Stock").get().val(), orient="index").reset_index()
    # generate the data for the plot
    name_col= dftable['Name'].tolist()*12
    month_col = []
    for i in range(12):
        for j in range(len(dftable["Name"])):
            month_col.append(i + 1)
    sold_col = choices(range(500,1000), k=12 * len(dftable['Name']))
    d = {'Name': name_col, 'Month': month_col, 'Sold':sold_col}
    # create DataFrame
    df2 = pd.DataFrame(data=d)
    # plot
    plot_div = plot(px.bar(df2, x="Month", y="Sold", color="Name", title="Mock Data For Report"), output_type='div')
    context = {'plot_div': plot_div}
    context['userlevel'] = request.session['userlevel']
    return render(request, "Report.html", context)

def AddStock(request):
    if request.method == 'POST':
        StockID = request.POST.get("StockID", "")
        StockName = request.POST.get("StockName", "")
        Price = int(request.POST.get("Price", ""))
        Qty = int(request.POST.get("Qty", ""))
        systemuser = {"StockID": StockID, "Name": StockName , "Price": Price,"Qty":Qty}
        db.child("Stock").push(systemuser)
        return HttpResponseRedirect('/stock/')
    else:
        return render(request, 'AddStock.html',{"userlevel":request.session['userlevel']})

def logout(request):
    try:
        del request.session['uid']
        del request.session['userlevel']
    except:
        pass
    return render(request,"login.html")

def handler404(request,exception):
    return render(request,"Error404.html")

def Purchase(request):
    if request.method == 'POST':
        if 'delete' in request.POST:
            Cart = db.child("SystemUser").order_by_child("Username").equal_to(request.session['username']).get()
            for val in Cart.each():
                db.child("SystemUser").child(val.key()).child('Cart').remove()
                return HttpResponseRedirect('/Purchase/')
        if 'Purchase' in request.POST:
            CartDict = db.child("SystemUser").order_by_child("Username").equal_to(request.session['username']).get().val()
            dftable = pd.DataFrame.from_dict(db.child("Stock").get().val(), orient="index").reset_index()
            df = pd.DataFrame(columns=['productid','productname','price','Qty','totalprice'])
            for val in CartDict:
                if 'Cart' in CartDict[val].keys():
                    CartDataFrame = pd.DataFrame.from_dict(CartDict[val]['Cart'], orient="index").reset_index()
                    for item in CartDataFrame['ProductID']:
                        Qty = request.POST.get(item)
                        Totalprice = (dftable.loc[dftable['StockID'] == item]['Price'].values[0] * int(Qty))
                        ProductName = (dftable.loc[dftable['StockID'] == item]['Name'].values[0])
                        ProductID = (dftable.loc[dftable['StockID'] == item]['StockID'].values[0])
                        UnitPrice = (dftable.loc[dftable['StockID'] == item]['Price'].values[0])
                        df = df.append({'productid':ProductID,'productname':ProductName,'price':UnitPrice,'Qty':Qty,'totalprice':Totalprice},ignore_index=True)
            request.session['Cart'] = df
            return  HttpResponseRedirect('/ConfirmCart/')
        return HttpResponseRedirect('/Purchase/')
    else:
         # get the firebase realtime directory and use the pandas to change the database and make it have columns
         CartDict = db.child("SystemUser").order_by_child("Username").equal_to(request.session['username']).get().val()
         data = []
         for val in CartDict:
            if 'Cart' in CartDict[val].keys():
                CartDataFrame = pd.DataFrame.from_dict(CartDict[val]['Cart'], orient="index").reset_index()
                json_records = CartDataFrame.reset_index().to_json(orient='records')
                data = json.loads(json_records)
         context = {'d': data}
         context['userlevel'] = request.session['userlevel']
         return render(request,"Shopping.html", context)

def AddPurchase(request):
    if request.method == 'POST':
        ProductID = request.POST.get("ProductID","")
        url="../AddCart/?ProductID="+ProductID
        return HttpResponseRedirect(url)
    else:
        return render(request,'AddPurchase.html')

def AddCart(request):
    if request.method == 'POST':
        ProductID = request.POST.get("ProductID", "")
        Name = request.POST.get("hiddenName", "")
        Price = request.POST.get("hiddenPrice", "")
        UserCart = {"ProductID":ProductID,"Name":Name,"Price":Price}
        UserItem = db.child("SystemUser").order_by_child("Username").equal_to(request.session['username']).get()
        for val in UserItem.each():
            db.child("SystemUser").child(val.key()).child('Cart').push(UserCart)
        return HttpResponseRedirect('/Purchase/')
    else:
        ProductID = request.GET.get('ProductID')
        # get the firebase realtime directory and use the pandas to change the database and make it have columns
        Stockdict = db.child("Stock").order_by_child("StockID").equal_to(ProductID).get().val()
        # Customer directory always have one
        for val in Stockdict.values():
            Stocks = val
        return render(request, 'ConProductinf.html', dict(Stocks))

def ConfirmCart(request):
        context = {'d': request.session['Cart']}
        context['userlevel'] = request.session['userlevel']
        return render(request, "ConfirmCart.html",context)
















