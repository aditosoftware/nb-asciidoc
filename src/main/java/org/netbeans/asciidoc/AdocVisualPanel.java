package org.netbeans.asciidoc;

import java.util.Objects;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javax.swing.JPanel;
import org.asciidoctor.Attributes;
import org.asciidoctor.AttributesBuilder;
import org.asciidoctor.Options;
import org.asciidoctor.OptionsBuilder;
import org.asciidoctor.SafeMode;
import org.jtrim.concurrent.GenericUpdateTaskExecutor;
import org.jtrim.concurrent.TaskExecutor;
import org.jtrim.concurrent.UpdateTaskExecutor;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;

@SuppressWarnings("serial")
public final class AdocVisualPanel extends JPanel {
    private static final Logger LOGGER = Logger.getLogger(AdocVisualPanel.class.getName());

    private final UpdateTaskExecutor adocUpdater;
    private final UpdateTaskExecutor htmlComponentUpdater;

    private final JFXPanel jfxPanel;

    private WebView webView;

    public AdocVisualPanel() {
        this(AdocExecutors.DEFAULT_EXECUTOR);
    }

    public AdocVisualPanel(TaskExecutor executor) {
        adocUpdater = new GenericUpdateTaskExecutor(executor);
        htmlComponentUpdater = new GenericUpdateTaskExecutor(Platform::runLater);
        webView = null;

        initComponents();

        jfxPanel = new JFXPanel();
        add(jfxPanel);
    }

    public void updateWithAsciidoc(Supplier<String> asciidocProvider) {
        Objects.requireNonNull(asciidocProvider, "asciidocProvider");

        adocUpdater.execute(() -> {
            ProgressHandle handle = ProgressHandleFactory.createHandle("Rendering Asciidoc ...");
            handle.start();
            try {
                String asciidocText = asciidocProvider.get();
                String html = AsciidoctorConverter.getDefault().convert(asciidocText, getInitialOptions());

                htmlComponentUpdater.execute(() -> updateHtmlNow(html));
            } catch (Throwable ex) {
                // TODO: Show the problem to the user.
                LOGGER.log(Level.INFO, "Failed to convert text to html.", ex);
            } finally {
                handle.finish();
            }
        });
    }

    private void updateHtmlNow(String html) {
        WebView currentWebView = webView;
        if (currentWebView == null) {
            currentWebView = new WebView();
            jfxPanel.setScene(new Scene(currentWebView));
            webView = currentWebView;
        }

        currentWebView.getEngine().loadContent(html);
    }

    public Options getInitialOptions() {
        Attributes attrs = AttributesBuilder.attributes()
                .showTitle(true)
                .sourceHighlighter("coderay")
                .attribute("coderay-css", "style")
                .get();
        OptionsBuilder opts = OptionsBuilder.options()
                .safe(SafeMode.SAFE)
                .backend("html5")
                .headerFooter(true)
                .attributes(attrs);
        return opts.get();
    }

    @Override
    public String getName() {
        return "AdocVisualElement";
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.GridLayout(1, 1));
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
