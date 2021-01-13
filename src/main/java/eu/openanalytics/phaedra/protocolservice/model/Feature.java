package eu.openanalytics.phaedra.protocolservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

public class Feature {

    @Id
    @Column("FEATURE_ID")
    private Long id;
    @Column("FEATURE_NAME")
    private String name;
    @Column("SHORT_NAME")
    private String shortName;
    @Column("PROTOCOL_ID")
    private Long protocolId;
    @Column("IS_NUMERIC")
    private boolean numeric = false;
    @Column("IS_LOGARITHMIC")
    private boolean logarithmic = false;
    @Column("IS_REQUIRED")
    private boolean required = false;
    @Column("IS_KEY")
    private boolean keyFeature = false;
    @Column("IS_UPLOADED")
    private boolean toUpload = false;
    @Column("IS_ANNOTATION")
    private boolean annotation = false;
    @Column("IS_CLASSIFICATION_RESTRICTED")
    private boolean classificationRestricted = false;
    @Column("CURVE_NORMALIZATION")
    private String curveNormalization;
    @Column("NORMALIZATION_LANGUAGE")
    private String normalizationLanguage;
    @Column("NORMALIZATION_FORMULA")
    private String normalizationFormula;
    @Column("NORMALIZATION_SCOPE")
    private Integer normalizationScope;
    private String description;
    @Column("FORMAT_STRING")
    private String formatString;
    @Column("LOW_WELLTYPE")
    private String lowWellType;
    @Column("HIGH_WELLTYPE")
    private String highWellType;
    @Column("CALC_FORMULA")
    private String calculationFormula;
    @Column("CALC_LANGUAGE")
    private String calculationLanguage;
    @Column("CALC_FORMULA_ID")
    private Long calculationFormulaId;
    @Column("CALC_TRIGGER")
    private String calculationTrigger;
    @Column("CALC_SEQUENCE")
    private Integer calculationSequence;
    @Column("GROUP_ID")
    private Long featureGroupId;

    public Feature() {
    }

    public Feature(Long protocolId) {
        this.protocolId = protocolId;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public boolean getNumeric() {
        return numeric;
    }

    public void setNumeric(boolean numeric) {
        this.numeric = numeric;
    }

    public boolean getLogarithmic() {
        return logarithmic;
    }

    public void setLogarithmic(boolean logarithmic) {
        this.logarithmic = logarithmic;
    }

    public boolean getRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public boolean getKeyFeature() {
        return keyFeature;
    }

    public void setKeyFeature(boolean keyFeature) {
        this.keyFeature = keyFeature;
    }

    public boolean getToUpload() {
        return toUpload;
    }

    public void setToUpload(boolean toUpload) {
        this.toUpload = toUpload;
    }

    public boolean getAnnotation() {
        return annotation;
    }

    public void setAnnotation(boolean annotation) {
        this.annotation = annotation;
    }

    public boolean getClassificationRestricted() {
        return classificationRestricted;
    }

    public void setClassificationRestricted(boolean classificationRestricted) {
        this.classificationRestricted = classificationRestricted;
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

    public String getLowWellType() {
        return lowWellType;
    }

    public void setLowWellType(String lowWellType) {
        this.lowWellType = lowWellType;
    }

    public String getHighWellType() {
        return highWellType;
    }

    public void setHighWellType(String highWellType) {
        this.highWellType = highWellType;
    }

    public String getCalculationFormula() {
        return calculationFormula;
    }

    public void setCalculationFormula(String calculationFormula) {
        this.calculationFormula = calculationFormula;
    }

    public String getCalculationLanguage() {
        return calculationLanguage;
    }

    public void setCalculationLanguage(String calculationLanguage) {
        this.calculationLanguage = calculationLanguage;
    }

    public Long getCalculationFormulaId() {
        return calculationFormulaId;
    }

    public void setCalculationFormulaId(Long calculationFormulaId) {
        this.calculationFormulaId = calculationFormulaId;
    }

    public String getCalculationTrigger() {
        return calculationTrigger;
    }

    public void setCalculationTrigger(String calculationTrigger) {
        this.calculationTrigger = calculationTrigger;
    }

    public Integer getCalculationSequence() {
        return calculationSequence;
    }

    public void setCalculationSequence(Integer calculationSequence) {
        this.calculationSequence = calculationSequence;
    }

    public Long getFeatureGroupId() {
        return featureGroupId;
    }

    public void setFeatureGroupId(Long featureGroupId) {
        this.featureGroupId = featureGroupId;
    }
}
