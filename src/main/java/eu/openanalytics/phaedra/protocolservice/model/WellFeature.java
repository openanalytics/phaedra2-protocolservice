package eu.openanalytics.phaedra.protocolservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

public class WellFeature {

    @Id
    private Long featureId;
    private String featureName;
    private String shortName;
    private Long protocolId;
    private boolean isNumeric = false;
    private boolean isLogarithmic = false;
    private boolean isRequired = false;
    private boolean isKey = false;
    private boolean isUploaded = false;
    private boolean isAnnotation = false;
    private boolean isClassificationRestricted = false;
    private String curveNormalization;
    private String normalizationLanguage;
    private String normalizationFormula;
    private Integer normalizationScope;
    private String description;
    private String formatString;
    private String lowWelltype;
    private String highWelltype;
    private String calcFormula;
    private String calcLanguage;
    private Long calcFormulaId;
    private String calcTrigger;
    private Integer calcSequence;
    private Long groupId;

    public WellFeature() {
    }

    public WellFeature(Long protocolId) {
        this.protocolId = protocolId;
    }

    public Long getFeatureId() {
        return featureId;
    }

    public String getFeatureName() {
        return featureName;
    }

    public void setFeatureName(String featureName) {
        this.featureName = featureName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public Long getProtocolId() {
        return protocolId;
    }

    public void setProtocolId(Long protocolId) {
        this.protocolId = protocolId;
    }

    public boolean isNumeric() {
        return isNumeric;
    }

    public void setNumeric(boolean numeric) {
        isNumeric = numeric;
    }

    public boolean isLogarithmic() {
        return isLogarithmic;
    }

    public void setLogarithmic(boolean logarithmic) {
        isLogarithmic = logarithmic;
    }

    public boolean isRequired() {
        return isRequired;
    }

    public void setRequired(boolean required) {
        isRequired = required;
    }

    public boolean isKey() {
        return isKey;
    }

    public void setKey(boolean key) {
        isKey = key;
    }

    public boolean isUploaded() {
        return isUploaded;
    }

    public void setUploaded(boolean uploaded) {
        isUploaded = uploaded;
    }

    public boolean isAnnotation() {
        return isAnnotation;
    }

    public void setAnnotation(boolean annotation) {
        isAnnotation = annotation;
    }

    public boolean isClassificationRestricted() {
        return isClassificationRestricted;
    }

    public void setClassificationRestricted(boolean classificationRestricted) {
        isClassificationRestricted = classificationRestricted;
    }

    public String getCurveNormalization() {
        return curveNormalization;
    }

    public void setCurveNormalization(String curveNormalization) {
        this.curveNormalization = curveNormalization;
    }

    public String getNormalizationLanguage() {
        return normalizationLanguage;
    }

    public void setNormalizationLanguage(String normalizationLanguage) {
        this.normalizationLanguage = normalizationLanguage;
    }

    public String getNormalizationFormula() {
        return normalizationFormula;
    }

    public void setNormalizationFormula(String normalizationFormula) {
        this.normalizationFormula = normalizationFormula;
    }

    public Integer getNormalizationScope() {
        return normalizationScope;
    }

    public void setNormalizationScope(Integer normalizationScope) {
        this.normalizationScope = normalizationScope;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFormatString() {
        return formatString;
    }

    public void setFormatString(String formatString) {
        this.formatString = formatString;
    }

    public String getLowWelltype() {
        return lowWelltype;
    }

    public void setLowWelltype(String lowWelltype) {
        this.lowWelltype = lowWelltype;
    }

    public String getHighWelltype() {
        return highWelltype;
    }

    public void setHighWelltype(String highWelltype) {
        this.highWelltype = highWelltype;
    }

    public String getCalcFormula() {
        return calcFormula;
    }

    public void setCalcFormula(String calcFormula) {
        this.calcFormula = calcFormula;
    }

    public String getCalcLanguage() {
        return calcLanguage;
    }

    public void setCalcLanguage(String calcLanguage) {
        this.calcLanguage = calcLanguage;
    }

    public Long getCalcFormulaId() {
        return calcFormulaId;
    }

    public void setCalcFormulaId(Long calcFormulaId) {
        this.calcFormulaId = calcFormulaId;
    }

    public String getCalcTrigger() {
        return calcTrigger;
    }

    public void setCalcTrigger(String calcTrigger) {
        this.calcTrigger = calcTrigger;
    }

    public Integer getCalcSequence() {
        return calcSequence;
    }

    public void setCalcSequence(Integer calcSequence) {
        this.calcSequence = calcSequence;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }
}
