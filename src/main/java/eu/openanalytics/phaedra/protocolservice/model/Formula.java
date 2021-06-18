package eu.openanalytics.phaedra.protocolservice.model;

import eu.openanalytics.phaedra.protocolservice.enumeration.FormulaCategory;
import eu.openanalytics.phaedra.protocolservice.enumeration.Scope;
import eu.openanalytics.phaedra.protocolservice.enumeration.ScriptLanguage;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

public class Formula {
    @Id
    @Column("formula_id")
    private Long formulaId;
    @Column("formula_name")
    private String formulaName;
    private String description;
    private FormulaCategory category;
    private ScriptLanguage language;
    private Scope scope;
    private String author;

    public Long getFormulaId() {
        return formulaId;
    }

    public void setFormulaId(Long formulaId) {
        this.formulaId = formulaId;
    }

    public String getFormulaName() {
        return formulaName;
    }

    public void setFormulaName(String formulaName) {
        this.formulaName = formulaName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public FormulaCategory getCategory() {
        return category;
    }

    public void setCategory(FormulaCategory category) {
        this.category = category;
    }

    public ScriptLanguage getLanguage() {
        return language;
    }

    public void setLanguage(ScriptLanguage language) {
        this.language = language;
    }

    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
