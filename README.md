## Android Forms Made Easy
> **NOTE:** this library only works with [AndroidX](https://developer.android.com/jetpack/androidx/migrate):



Include this dependency to your build.gradle file

```` groovy
implementation 'com.hurtado.forms:AndroidForms:0.1.0'
````

# Form Group 

First add your **FromGroup** to your **XML file** this will be your containter
for all your form fields.

This class inherits from **ConstraintLayout** meaning the same rules will apply 
as if you were using itt as a parent.

```` xml
<?xml version="1.0" encoding="utf-8"?>
<com.hurtado.forms.widget.FormGroup
        xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:app="http://schemas.android.com/apk/res-auto"
        android:orientation="vertical"
        android:layout_width="match_parent"
        android:layout_height="match_parent">

</com.hurtado.forms.widget.FormGroup>
````

# Form Fields 

Now that we have our parent class lets start adding our **FormField**'s
A **FormField** is just a fancy name for a **TextInputLayout**, with just
Enough code to make things work.

And of course we will need a **TextInputEditText** inside our **FormField**

```` xml
<?xml version="1.0" encoding="utf-8"?>
<com.hurtado.forms.widget.FormGroup
        xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:app="http://schemas.android.com/apk/res-auto"
        android:orientation="vertical"
        android:id="@+id/form_group"
        android:layout_width="match_parent"
        android:layout_height="match_parent">

    <com.hurtado.forms.widget.FormField
            android:id="@+id/input_layout"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintHorizontal_bias="0.5"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintTop_toTopOf="parent"
            app:layout_constraintBottom_toBottomOf="parent">

        <com.google.android.material.textfield.TextInputEditText
                android:hint="@string/sample_validation"
                android:id="@+id/edit_text"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"/>
    </com.hurtado.forms.widget.FormField>

</com.hurtado.forms.widget.FormGroup>
````
# Validations 

you will need to create an array of validations for your **FormField** to read,
decalre it on your **strings.xml** file, you can add as many as you want.

```` xml
<resources>

    <array name="sample_field_requirements">
        <item>com.hurtado.forms.validations.RequiredValidation</item>
        <item>com.hurtado.forms.validations.MaxCharValidation</item>
        <item>com.hurtado.forms.validations.SymbolsValidation</item>
    </array>

</resources>

````

# Pre-built Validations

```` com.hurtado.forms.validations.MaxCharValidation````

```` com.hurtado.forms.validations.MinCharValidation````

```` com.hurtado.forms.validations.EmailValidation````

```` com.hurtado.forms.validations.SymbolsValidation````

```` com.hurtado.forms.validations.RequiredValidation````


**Do not repeat the same validation twice !!**

**It will throw an exception :bug:**

# Bind it all together
Now just add the property ```` app:validations="@array/sample_field_requirements" ```` 
to your **FormField** and **tha's it !!**

As a bonus you can add a **Button** inside your **FormGroup** and it will be **enable / disable**
Automatically depending on the form status **form valid / form invalid**

```` xml
<?xml version="1.0" encoding="utf-8"?>
<com.hurtado.forms.widget.FormGroup
        xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:app="http://schemas.android.com/apk/res-auto"
        android:orientation="vertical"
        android:id="@+id/form_group"
        android:layout_width="match_parent"
        android:layout_height="match_parent">

    <com.hurtado.forms.widget.FormField
            android:id="@+id/input_layout_1"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintHorizontal_bias="0.5"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintTop_toTopOf="parent"
            app:validations="@array/sample_field_requirements"
            app:layout_constraintBottom_toBottomOf="parent">

        <com.google.android.material.textfield.TextInputEditText
                android:hint="@string/sample_validation"
                android:id="@+id/edit_text_1"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"/>
    </com.hurtado.forms.widget.FormField>


    <Button
            android:layout_marginTop="24dp"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintEnd_toEndOf="parent"
            android:text="@string/submit_form"
            android:backgroundTint="@color/colorPrimary"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            app:layout_constraintHorizontal_bias="0.5"
            app:layout_constraintTop_toBottomOf="@id/input_layout_1"/>

</com.hurtado.forms.widget.FormGroup>
````

# Creating custom validations
It's great to have some prebuilt validations on our forms, but what if we would like
Something custom ?

**Just extend Validation class and its all done!!**
override onValidate function and validate your edit text input agains your own logic

```` kotlin
package com.hurtado.validation.validations

import ...
import ...

class CustomValidation: Validation(R.string.custom_error) {

    override fun onValidate(context: Context?, value: String): String? {
        // Error is triggered if input contains letter A
        return if (value.contains("A"))
            super.onValidate(context, value)
        else
            null
    }
}
````

Now just add it to your array of validations same as the preloaded ones of course using your own package name

```` xml
<resources>
    <string name="app_name">validation</string>

    ...
    ...
    ...

    <array name="sample_field_requirements">
        ...
        ...
        <item>com.hurtado.validation.validations.CustomValidation</item>
    </array>

</resources>
````

**And that's it, now you have your custom validation !!**
Please check our fully functional example on this repo

# Happy Coding  :beers:
