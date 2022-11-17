import gtk4.*
import kotlinx.cinterop.*

fun main() {
    val app = gtk_application_new("org.gtk.example", G_APPLICATION_HANDLES_OPEN)

    g_signal_connect_data(
        app,
        "activate",
        staticCFunction<CPointer<GtkApplication>?, gpointer?, Unit> { app, _ ->
            val window = gtk_application_window_new(app)
            gtk_window_set_title(window as CValuesRef<GtkWindow>, "标题")
            gtk_window_set_default_size(window as CValuesRef<GtkWindow>, 400, 400)
            val box = gtk_box_new(GtkOrientation.GTK_ORIENTATION_VERTICAL, 0)
            gtk_widget_set_halign(box, GtkAlign.GTK_ALIGN_CENTER)
            gtk_widget_set_valign(box, GtkAlign.GTK_ALIGN_CENTER)
            gtk_window_set_child(window as CValuesRef<GtkWindow>, box)
            val btn = gtk_button_new_with_label("请点击我")
            g_signal_connect_data(
                btn,
                "clicked",
                staticCFunction<CPointer<GtkButton>?, gpointer?, Unit> { button, _ ->
                    gtk_button_set_label(button, "你点击了我")
                } as GCallback,
                null,
                null,
                1
            )
            gtk_box_append(box as CPointer<GtkBox>, btn)
            gtk_widget_show(window)
        } as GCallback,
        null,
        null,
        1
    )
    g_application_run(app as CValuesRef<GApplication>, 0, null)
    g_object_unref(app)
}
