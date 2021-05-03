# Author By Koo Chung Hing

from django.urls import path , re_path
from django.conf.urls import url
from . import views


urlpatterns = [
    path('', views.login),
    path('home/', views.home,name="home"),
    path('product/', views.Product,name="Product"),
    path('stock/', views.Store,name="stock"),
    path('userbalance/', views.balance,name="balance"),
    path('Customer/', views.Customer,name="Customer"),
    path('SystemAccount/', views.SystemAccount,name="SystemAccount"),
    path('AddSystemAccount/', views.AddSystemAccount,name="AddSystemAccount"),
    path('AddStock/', views.AddStock,name="AddStock"),
    path('AddPurchase/', views.AddPurchase,name="AddPurchase"),
    path('Report/', views.Report,name="Report"),
    re_path(r'^EditStock/$', views.EditStock),
    re_path(r'^EditProduct/$', views.EditProduct),
    path('Report/', views.Report,name="Report"),
    url(r'^logout/', views.logout,name="log"),
    url(r'^$', views.handler404,name="error404"),
    path('Purchase/', views.Purchase,name="Purchase"),
    path('AddCart/', views.AddCart,name="AddCart"),
    path('ConfirmCart/', views.ConfirmCart,name="ConfirmCart"),
    path('CustomerPayment/', views.CustomerPayment, name="CustomerPayment"),
]


