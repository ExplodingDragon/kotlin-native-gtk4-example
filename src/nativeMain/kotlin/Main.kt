@file:Suppress("UNCHECKED_CAST")

import adwaita.*
import kotlinx.cinterop.*
import platform.posix.*

fun main() {
    val app = gtk_application_new("org.gtk.example", G_APPLICATION_HANDLES_OPEN)

    g_signal_connect_data(
        app,
        "activate",
        staticCFunction<CPointer<GtkApplication>?, gpointer?, Unit> { application, _ ->
            val window = (gtk_application_window_new(application) as CValuesRef<GtkWindow>).apply {
                gtk_window_set_focus_visible(this, TRUE)
                gtk_window_set_resizable(this, FALSE)
                gtk_window_set_title(this, "标题")
                val display = gtk_icon_theme_get_for_display(gdk_display_get_default())
                if (gtk_icon_theme_has_icon(display, "firefox") == TRUE) {
                    gtk_window_set_icon_name(this, "firefox")
                }
                gtk_window_set_default_size(this, 640, 400)
            }
            val box = gtk_box_new(GtkOrientation.GTK_ORIENTATION_VERTICAL, 0).apply {
                gtk_widget_set_halign(this, GtkAlign.GTK_ALIGN_CENTER)
                gtk_widget_set_valign(this, GtkAlign.GTK_ALIGN_CENTER)
            }
            gtk_window_set_child(window, box)
            val btn = gtk_button_new_with_label("请点击我").apply {
                g_signal_connect_data(
                    this,
                    "clicked",
                    staticCFunction<CPointer<GtkButton>?, gpointer?, Unit> { button, _ ->
                        gtk_button_set_label(button, "你点击了我")
                    } as GCallback,
                    null,
                    null,
                    1
                )
            }
            gtk_box_append(box as CPointer<GtkBox>, btn)
            gtk_widget_show(window as CValuesRef<GtkWidget>)
        } as GCallback,
        null,
        null,
        1
    )
    g_application_run(app as CValuesRef<GApplication>, 0, null)
    g_object_unref(app)
}
