package org.eth.demo.sebserver.gui.view;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class ViewService {

    private final ApplicationContext applicationContext;
    private final Class<? extends ViewComposer> initViewComposer;

    public ViewService(
            final ApplicationContext applicationContext,
            final Class<? extends ViewComposer> initViewComposer) {

        this.applicationContext = applicationContext;
        this.initViewComposer = initViewComposer;
    }

    public void composeView(final Composite parent, final Class<? extends ViewComposer> composerType) {
        clearView(parent);
        final ViewComposer composer = this.applicationContext.getBean(composerType);
        composer.composeView(this, parent);
        parent.layout();
    }

    public void composeInitView(final Composite parent) {
        composeView(parent, this.initViewComposer);
    }

    private void clearView(final Composite parent) {
        for (final Control control : parent.getChildren()) {
            control.dispose();
        }
    }

}
