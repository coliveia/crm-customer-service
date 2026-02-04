package com.vivo.crm.customer.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * TMF629 - Customer Entity
 * Represents a customer (inherits from PartyRole)
 * Mapped directly to CUSTOMER table (not using Duality View)
 * 
 * Campos adicionados conforme planilha CamposParaAdicionar.xlsx:
 * - formattedName: Nome completo do cliente (TMF632 Individual)
 * - givenName: Primeiro nome (TMF632 Individual)
 * - familyName: Sobrenome (TMF632 Individual)
 * - preferredGivenName: Nome de preferência (TMF632 Individual)
 * - tradingName: Razão social para PJ (TMF632 Organization)
 * - identificationType: Tipo de documento (CPF/CNPJ)
 * - identificationNumber: Número do documento
 * - creditScore: Score de crédito do cliente (TMF669)
 * - creditRiskRating: Rating de risco de crédito
 * - biometriaColetada: Status da biometria
 * - codigoGrupo: Código do grupo/hierarquia
 * - nomeGrupo: Nome do grupo (CORPORATE, GOVERNO)
 */
@Entity
@Table(name = "customer")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {

    @Id
    @Column(name = "customer_id", length = 50)
    private String customerId;

    @Column(name = "party_role_id", length = 50, nullable = false)
    private String partyRoleId;

    @Column(name = "external_id", length = 100)
    private String externalId;

    @Column(name = "customer_name", length = 255, nullable = false)
    private String name;

    // ========== CAMPOS ADICIONADOS - TMF632 Individual ==========

    /**
     * Nome completo formatado do cliente (TMF632 Individual.formattedName)
     * Exemplo: "João da Silva Santos"
     */
    @Column(name = "formatted_name", length = 255)
    private String formattedName;

    /**
     * Primeiro nome do cliente (TMF632 Individual.givenName)
     * Exemplo: "João"
     */
    @Column(name = "given_name", length = 100)
    private String givenName;

    /**
     * Sobrenome/Nome de família (TMF632 Individual.familyName)
     * Exemplo: "Santos"
     */
    @Column(name = "family_name", length = 100)
    private String familyName;

    /**
     * Nome de preferência escolhido pelo cliente no APP (TMF632 Individual)
     * Exemplo: "Johne" - nome que o cliente deseja ser chamado
     */
    @Column(name = "preferred_given_name", length = 100)
    private String preferredGivenName;

    // ========== CAMPOS ADICIONADOS - TMF632 Organization ==========

    /**
     * Razão Social da empresa (TMF632 Organization.tradingName)
     * Exemplo: "Souza e Silva Ltda"
     */
    @Column(name = "trading_name", length = 255)
    private String tradingName;

    // ========== CAMPOS ADICIONADOS - TMF632 Party Identification ==========

    /**
     * Tipo de identificação: CPF, CNPJ, RG, PASSPORT
     * (TMF632 IndividualIdentification/OrganizationIdentification)
     */
    @Column(name = "identification_type", length = 20)
    private String identificationType;

    /**
     * Número do documento de identificação (sem formatação)
     * Exemplo: "12345678900" (CPF) ou "56989093000182" (CNPJ)
     */
    @Column(name = "identification_number", length = 20)
    private String identificationNumber;

    // ========== CAMPOS ADICIONADOS - TMF669 Credit Profile ==========

    /**
     * Score de crédito do cliente (TMF669 PartyRole.creditScore)
     * Representa o histórico do cliente na Vivo (compras, pagamentos)
     * Exemplo: 750 pontos
     */
    @Column(name = "credit_score")
    private Integer creditScore;

    /**
     * Rating de risco de crédito (TMF629 CreditProfile.creditRiskRating)
     * Valores típicos: 1-10 onde 1 é baixo risco e 10 é alto risco
     */
    @Column(name = "credit_risk_rating")
    private Integer creditRiskRating;

    // ========== CAMPOS ADICIONADOS - Características de Negócio ==========

    /**
     * Status da biometria do cliente
     * Valores: "COLETADA", "PENDENTE", "NAO_COLETADA"
     * Mensagem retornada: "Biometrado" ou "Valide o token"
     */
    @Column(name = "biometria_status", length = 50)
    private String biometriaStatus;

    /**
     * Código do grupo/hierarquia da empresa
     * Exemplo: "4042591651"
     */
    @Column(name = "codigo_grupo", length = 50)
    private String codigoGrupo;

    /**
     * Nome do grupo (CORPORATE, GOVERNO)
     * Exemplo: "CORPORATE"
     */
    @Column(name = "nome_grupo", length = 100)
    private String nomeGrupo;

    // ========== CAMPOS EXISTENTES ==========

    @Column(name = "email", length = 255)
    private String email;

    @Column(name = "phone", length = 20)
    private String phone;

    /**
     * @deprecated Use identificationType e identificationNumber
     */
    @Deprecated
    @Column(name = "cpf_cnpj", length = 20)
    private String cpfCnpj;

    @Column(name = "segment", length = 100)
    private String segment;

    @Column(name = "preferred_channel", length = 50)
    private String preferredChannel;

    @Column(name = "risk_level", length = 50)
    private String riskLevel; // LOW, MEDIUM, HIGH

    @Column(name = "status", length = 50, nullable = false)
    private String status; // ACTIVE, INACTIVE, PROSPECT, SUSPENDED

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "created_by", length = 100)
    private String createdBy;

    @Column(name = "updated_by", length = 100)
    private String updatedBy;

    @Column(name = "context_data", columnDefinition = "JSON")
    private String contextData;

    @PrePersist
    protected void onCreate() {
        if (customerId == null) {
            customerId = java.util.UUID.randomUUID().toString();
        }
        if (partyRoleId == null) {
            partyRoleId = java.util.UUID.randomUUID().toString();
        }
        if (status == null) {
            status = "ACTIVE";
        }
        // Migrar cpfCnpj para os novos campos se necessário
        if (cpfCnpj != null && identificationNumber == null) {
            identificationNumber = cpfCnpj.replaceAll("[^0-9]", "");
            identificationType = identificationNumber.length() == 11 ? "CPF" : "CNPJ";
        }
    }

    /**
     * Ativa o cliente
     */
    public void activate(String actor) {
        this.status = "ACTIVE";
        this.updatedBy = actor;
    }

    /**
     * Desativa o cliente
     */
    public void deactivate(String actor) {
        this.status = "INACTIVE";
        this.updatedBy = actor;
    }

    /**
     * Suspende o cliente
     */
    public void suspend(String reason, String actor) {
        this.status = "SUSPENDED";
        this.riskLevel = "HIGH";
        this.updatedBy = actor;
    }

    /**
     * Verifica se o cliente está ativo
     */
    public boolean isActive() {
        return "ACTIVE".equals(this.status);
    }

    /**
     * Verifica se o cliente está suspenso
     */
    public boolean isSuspended() {
        return "SUSPENDED".equals(this.status);
    }

    /**
     * Verifica se é pessoa física (CPF)
     */
    public boolean isIndividual() {
        return "CPF".equals(this.identificationType) || "Individual".equals(this.getCustomerType());
    }

    /**
     * Verifica se é pessoa jurídica (CNPJ)
     */
    public boolean isOrganization() {
        return "CNPJ".equals(this.identificationType) || "Organization".equals(this.getCustomerType());
    }

    /**
     * Retorna o tipo de cliente baseado na identificação
     */
    public String getCustomerType() {
        if (identificationType != null) {
            return "CPF".equals(identificationType) ? "Individual" : "Organization";
        }
        if (identificationNumber != null) {
            return identificationNumber.length() == 11 ? "Individual" : "Organization";
        }
        return null;
    }

    /**
     * Retorna mensagem de biometria para o atendente
     */
    public String getBiometriaMessage() {
        if ("COLETADA".equals(biometriaStatus)) {
            return "Biometrado";
        }
        return "Valide o token";
    }
}
