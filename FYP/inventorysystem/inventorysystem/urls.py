# Author By Koo Chung Hing

from django.contrib import admin
from django.urls import path , include

urlpatterns = [
    path('admin/', admin.site.urls),
    path('', include('store.urls')),
]


handler404 = 'store.views.handler404'
