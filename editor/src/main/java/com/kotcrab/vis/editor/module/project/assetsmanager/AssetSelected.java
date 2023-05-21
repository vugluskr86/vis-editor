package com.kotcrab.vis.editor.module.project.assetsmanager;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.Layout;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.kotcrab.vis.editor.module.ModuleInjector;
import com.kotcrab.vis.editor.module.project.SceneTabsModule;
import com.kotcrab.vis.editor.module.scene.entitymanipulator.EntityManipulatorModule;
import com.kotcrab.vis.editor.ui.scene.SceneTab;
import com.kotcrab.vis.ui.widget.tabbedpane.Tab;

/**
 * Assets manager drag and drop helper class.
 * @author vugluskr86
 */
public class AssetSelected implements Disposable {
    private Array<AtlasItem> selected = new Array<>();

    private AtlasItem brushActor = null;

    private Array<AtlasItem> items = new Array<>();

    private Tab tab = null;

    private SceneTabsModule sceneTabsModule;

    public AssetSelected(ModuleInjector injector) {
        injector.injectModules(this);
    }

    public void addSources (Array<AtlasItem> items) {
        for (AtlasItem item : items)
            addAtlasSource(item);
    }

    private void addAtlasSource (AtlasItem sourceItem) {
        items.add(sourceItem);
        sourceItem.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                SceneTab sceneTab = (SceneTab)tab;

                for (AtlasItem item : items) {
                    item.setSelected(false);
                }
                sourceItem.setSelected(true);

                if (brushActor != null) {
                    brushActor.remove();
                }

                brushActor = new AtlasItem(sourceItem.getRelativeAtlasPath(), sourceItem.getRegion(), sourceItem.isShowLabel(), true);
                brushActor.setTouchable(Touchable.disabled);
                if (sceneTab != null) {
                    sceneTab.getStage().addActor(brushActor);
                }
                brushActor.setSize(32, 32);
                brushActor.setZIndex(10000);
            }
        });
    }

/*
    public boolean mouseMoved(InputEvent event, float x, float y) {
        if (brushActor != null) {
            brushActor.setPosition(x, y);
        }
        return false;
    }
*/
/*
    private void addItemSelected(AtlasItem item) {
        if (!selected.contains(item, true)) {
            selected.add(item);
        }
    }

    private void removeItemSelected(AtlasItem item) {
        if (selected.contains(item, true)) {
            selected.removeValue(item, true);
        }
    }
*/

    public void addTargetTab(Tab tab) {
        this.tab = tab;
        if (this.tab instanceof SceneTab) {
            SceneTab sceneTab = (SceneTab)this.tab;
            if (brushActor != null && !sceneTab.getStage().getActors().contains(brushActor, true)) {
                sceneTab.getStage().addActor(brushActor);
            }
            sceneTab.getStage().addListener(new InputListener() {
                @Override
                public boolean mouseMoved(InputEvent event, float x, float y) {
                    if (brushActor != null) {
                        brushActor.setPosition(x, y);
                    }
                    return super.mouseMoved(event, x, y);
                }
            });
            sceneTab.getContentTable().addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    if (brushActor != null) {
                        EntityManipulatorModule entityManipulator = sceneTab.getEntityManipulator();
                        entityManipulator.processDropPayload(brushActor.getAtlasAsset());
                    }
                 }
            });
        }
    }

    @Override
    public void dispose() {
        selected.clear();
        items.clear();
    }
}
