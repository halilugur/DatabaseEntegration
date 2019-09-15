package com.ugurhalil.configuration;

/**
 * @author Halil UĞUR
 * @since 25.08.2019
 */
public class HibernateConfig {

    private boolean _activeHibernate;
    private String _hibernateFetchType;
    private String _hibernateCascadeType;
    private String _hibernatePackageName;
    private String _hibernateModelsGeneratedPath;
    private boolean _exportTableActive;

    public void setActiveHibernate(boolean activeHibernate)
    {
        _activeHibernate = activeHibernate;
    }

    public void setHibernateFetchType(String hibernateFetchType) {
        this._hibernateFetchType = hibernateFetchType;
    }

    public void setHibernateCascadeType(String hibernateCascadeType) {
        this._hibernateCascadeType = hibernateCascadeType;
    }

    public void setHibernatePackageName(String hibernatePackageName) {
        this._hibernatePackageName = hibernatePackageName;
    }

    public void setHibernateModelsGeneratedPath(String hibernateModelsGeneratedPath) {
        this._hibernateModelsGeneratedPath = hibernateModelsGeneratedPath;
    }

    public boolean is_activeHibernate() {
        return _activeHibernate;
    }

    public String get_hibernateFetchType() {
        return _hibernateFetchType;
    }

    public String get_hibernateCascadeType() {
        return _hibernateCascadeType;
    }

    public String get_hibernatePackageName() {
        return _hibernatePackageName;
    }

    public String get_hibernateModelsGeneratedPath() {
        return _hibernateModelsGeneratedPath;
    }

    public boolean isExportTableActive() {
        return _exportTableActive;
    }

    public void setExportTableActive(boolean exportTableActive) {
        this._exportTableActive = exportTableActive;
    }
}
