package me.sofiavicedomini.mboxconverter.controllers;

import lombok.Generated;
import lombok.Getter;
import lombok.Setter;
import me.sofiavicedomini.mboxconverter.SceneManager;

public abstract class Controller<T extends Controller.Data> {

    @Setter
    T data;

    public static abstract class Data {
    }

    public static class NoData extends Data {
    }

    protected SceneManager sceneManager;

    public void initialize(SceneManager sceneManager, Controller.Data data) {
        this.sceneManager = sceneManager;
        if (!(data instanceof NoData)) {
            this.data = (T) data;
        }
    }

}
