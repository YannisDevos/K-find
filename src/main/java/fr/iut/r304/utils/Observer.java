package fr.iut.r304.utils;

public interface Observer {
        void update(Observable observable);
        void update(Observable observable, Object data);
}
