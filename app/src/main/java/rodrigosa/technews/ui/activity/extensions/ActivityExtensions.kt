package rodrigosa.technews.ui.activity.extensions

import android.app.Activity
import android.widget.Toast

fun Activity.showError(menssage: String) {
    Toast.makeText(this, menssage, Toast.LENGTH_LONG).show()
}