package com.hurtado.validation

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.hurtado.forms.widget.FormGroup

class MainActivity : AppCompatActivity(), FormGroup.CompleteListener<SampleForm> {

    private lateinit var sampleForm: FormGroup<SampleForm>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sampleForm = findViewById(R.id.form_group)
        sampleForm.setOnCompleteListener(
            this,
            SampleForm()
        )
    }

    override fun onDestroy() {
        sampleForm.onDestroy()
        super.onDestroy()
    }

    override fun onFormComplete(result: SampleForm) {
        Log.d("RESULT", result.toString())
    }
}
