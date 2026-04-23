import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { map } from "rxjs/operators";
import { environment } from "../../../environments/environment";
import {
  Escola,
  Unidade,
  ProfissionalSaude,
  Medicamento,
  Atendimento,
  Prontuario,
  Requisicao,
  StatusDashboard,
  Paciente,
} from "../models/models";

export interface EscolaRequest {
  nome: string;
  ies: string;
  coordenador: string;
}

export interface UnidadeRequest {
  nome: string;
  ies: string;
  responsavel: string;
}

export interface ProfissionalCadastroRequest {
  nome: string;
  username: string;
  password: string;
}

export interface ProfissionalComplementoRequest {
  especialidade: string;
  formacao: string;
  conselho: string;
  numeroRegistro: string;
  diasAtendimento: string;
  turnosAtendimento: string;
}

export interface MedicamentoRequest {
  nome: string;
  descricao?: string;
  quantidade?: number;
  unidadeMedida?: string;
  ativo?: boolean;
}

export interface AtendimentoRequest {
  pacienteId: number;
  profissionalId?: number;
  dataInicio?: string;
  dataFim?: string;
  sintomas?: string;
  diagnostico?: string;
  tratamento?: string;
  tipo?: string;
  medicacoesUsadas?: Array<{
    medicacaoId: number;
    medicacaoNome?: string;
    quantidade: number;
    dosagem: string;
  }>;
  descricao?: string;
  observacoes?: string;
  dataAtendimento?: string;
  status?: string;
}

/**
 * PacienteRequest alinhado com o backend real.
 * CORREÇÃO: campos corretos (categoria, vinculoTipo, escolaId, unidadeId)
 * em vez dos antigos (cpf, dataNascimento, endereco, responsavel).
 */
export interface PacienteRequest {
  nome: string;
  email?: string;
  telefone?: string;
  categoria: string;
  vinculoTipo: string;
  vinculoNome?: string;
  escolaId?: number;
  unidadeId?: number;
}

@Injectable({ providedIn: "root" })
export class ApiService {
  private base = environment.apiUrl;

  constructor(private http: HttpClient) {}

  private adaptProfissional(item: Partial<ProfissionalSaude>): ProfissionalSaude {
    return {
      id: item.id ?? 0,
      nome: item.nome ?? "",
      username: item.username ?? "",
      usuarioId: item.usuarioId,
      formacao: item.formacao ?? "",
      conselho: item.conselho ?? "",
      especialidade: item.especialidade ?? "",
      numeroRegistro: item.numeroRegistro ?? "",
      diasAtendimento: item.diasAtendimento ?? "",
      turnosAtendimento: item.turnosAtendimento ?? "",
      dataCadastro: item.dataCadastro ?? "",
      status: item.status ?? "ATIVO",
      cadastroCompleto: item.cadastroCompleto ?? false,
    };
  }

  private adaptMedicamento(item: any): Medicamento {
    return {
      id: item.id ?? 0,
      nome: item.nome ?? "",
      descricao: item.descricao ?? "",
      fornecedor: item.fornecedor ?? "Nao informado",
      armazenamento: item.armazenamento ?? "TEMPERATURA_AMBIENTE",
      estoque: item.estoque ?? item.quantidade ?? 0,
      dataAquisicao: item.dataAquisicao ?? "",
      validade: item.validade ?? "",
      status: item.status ?? (item.ativo === false ? "INATIVO" : "ATIVO"),
    };
  }

  private encodeAtendimentoDetails(data: AtendimentoRequest): string {
    return JSON.stringify({
      diagnostico: data.diagnostico ?? "",
      tratamento: data.tratamento ?? "",
      dataFim: data.dataFim ?? "",
      tipo: data.tipo ?? data.status ?? "CONSULTA",
      medicacoesUsadas: data.medicacoesUsadas ?? [],
    });
  }

  private decodeAtendimentoDetails(raw?: string | null): {
    diagnostico?: string;
    tratamento?: string;
    dataFim?: string;
    tipo?: string;
    medicacoesUsadas?: Atendimento["medicacoesUsadas"];
  } {
    if (!raw) {
      return {};
    }

    try {
      return JSON.parse(raw);
    } catch {
      return { tratamento: raw };
    }
  }

  private adaptAtendimento(item: any): Atendimento {
    const details = this.decodeAtendimentoDetails(item.observacoes);

    return {
      id: item.id ?? 0,
      pacienteId: item.pacienteId ?? 0,
      pacienteNome: item.pacienteNome ?? "",
      profissionalId: item.profissionalId ?? item.profissionalUsuarioId ?? 0,
      profissionalNome: item.profissionalNome ?? "",
      dataInicio: item.dataInicio ?? item.dataAtendimento ?? "",
      dataFim: item.dataFim ?? details.dataFim,
      sintomas: item.sintomas ?? item.descricao ?? "",
      diagnostico: item.diagnostico ?? details.diagnostico ?? "",
      tratamento: item.tratamento ?? details.tratamento ?? "",
      tipo: item.tipo ?? item.status ?? details.tipo ?? "CONSULTA",
      medicacoesUsadas: item.medicacoesUsadas ?? details.medicacoesUsadas ?? [],
    };
  }

  private adaptProntuario(item: any): Prontuario {
    return {
      id: item.id ?? 0,
      pacienteId: item.pacienteId ?? 0,
      pacienteNome: item.pacienteNome ?? "",
      atendimentos: Array.isArray(item.atendimentos)
        ? item.atendimentos.map((atendimento: any) => this.adaptAtendimento(atendimento))
        : [],
    };
  }

  // ── ESCOLAS ────────────────────────────────────────────────────────────────

  getEscolas(): Observable<Escola[]> {
    return this.http.get<Escola[]>(`${this.base}/escolas`);
  }

  getEscolaById(id: number): Observable<Escola> {
    return this.http.get<Escola>(`${this.base}/escolas/${id}`);
  }

  criarEscola(data: EscolaRequest): Observable<Escola> {
    return this.http.post<Escola>(`${this.base}/escolas`, data);
  }

  atualizarEscola(id: number, data: EscolaRequest): Observable<Escola> {
    return this.http.put<Escola>(`${this.base}/escolas/${id}`, data);
  }

  inativarEscola(id: number): Observable<void> {
    return this.http.patch<void>(`${this.base}/escolas/${id}/inativar`, {});
  }

  // ── UNIDADES ───────────────────────────────────────────────────────────────

  getUnidades(): Observable<Unidade[]> {
    return this.http.get<Unidade[]>(`${this.base}/unidades`);
  }

  getUnidadeById(id: number): Observable<Unidade> {
    return this.http.get<Unidade>(`${this.base}/unidades/${id}`);
  }

  criarUnidade(data: UnidadeRequest): Observable<Unidade> {
    return this.http.post<Unidade>(`${this.base}/unidades`, data);
  }

  atualizarUnidade(id: number, data: UnidadeRequest): Observable<Unidade> {
    return this.http.put<Unidade>(`${this.base}/unidades/${id}`, data);
  }

  inativarUnidade(id: number): Observable<void> {
    return this.http.patch<void>(`${this.base}/unidades/${id}/inativar`, {});
  }

  // ── PROFISSIONAIS (admin) ──────────────────────────────────────────────────

  getProfissionais(): Observable<ProfissionalSaude[]> {
    return this.http
      .get<Partial<ProfissionalSaude>[]>(`${this.base}/profissionais`)
      .pipe(map((list) => list.map((item) => this.adaptProfissional(item))));
  }

  getProfissionalById(id: number): Observable<ProfissionalSaude> {
    return this.http
      .get<Partial<ProfissionalSaude>>(`${this.base}/profissionais/${id}`)
      .pipe(map((item) => this.adaptProfissional(item)));
  }

  criarProfissional(data: ProfissionalCadastroRequest): Observable<ProfissionalSaude> {
    return this.http
      .post<Partial<ProfissionalSaude>>(`${this.base}/profissionais`, data)
      .pipe(map((item) => this.adaptProfissional(item)));
  }

  inativarProfissional(id: number): Observable<void> {
    return this.http.patch<void>(`${this.base}/profissionais/${id}/inativar`, {});
  }

  // ── PROFISSIONAIS (self — perfil próprio) ──────────────────────────────────

  getMeuPerfil(): Observable<ProfissionalSaude> {
    return this.http
      .get<Partial<ProfissionalSaude>>(`${this.base}/profissionais/meu-perfil`)
      .pipe(map((item) => this.adaptProfissional(item)));
  }

  completarCadastro(data: ProfissionalComplementoRequest): Observable<ProfissionalSaude> {
    return this.http.patch<ProfissionalSaude>(
      `${this.base}/profissionais/meu-perfil/complemento`,
      data
    ).pipe(map((item) => this.adaptProfissional(item)));
  }

  // ── PACIENTES ──────────────────────────────────────────────────────────────

  /**
   * Retorna apenas os pacientes do profissional autenticado (filtrado no backend).
   */
  getPacientes(): Observable<Paciente[]> {
    return this.http.get<Paciente[]>(`${this.base}/paciente`);
  }

  /**
   * Cadastra um novo paciente vinculado ao profissional autenticado.
   */
  criarPaciente(data: PacienteRequest): Observable<Paciente> {
    return this.http.post<Paciente>(`${this.base}/paciente`, data);
  }

  /**
   * Atualiza os dados de um paciente existente.
   * CORREÇÃO: método inexistente — pacientes.component.ts chamava este método
   * mas ele não existia, fazendo a edição nunca persistir no banco.
   *
   * Chama: PUT /paciente/{id}
   * Backend: PacienteController.updatePaciente() → PacienteService.updatePaciente()
   */
  atualizarPaciente(id: number, data: PacienteRequest): Observable<Paciente> {
    return this.http.put<Paciente>(`${this.base}/paciente/${id}`, data);
  }

  inativarPaciente(id: number): Observable<void> {
    return this.http.put<void>(`${this.base}/paciente/inativar/${id}`, {});
  }

  // ── MEDICAMENTOS ───────────────────────────────────────────────────────────

  getMedicamentos(): Observable<Medicamento[]> {
    return this.http
      .get<any[]>(`${this.base}/medicamento`)
      .pipe(map((list) => list.map((item) => this.adaptMedicamento(item))));
  }

  criarMedicamento(data: MedicamentoRequest): Observable<Medicamento> {
    return this.http
      .post<any>(`${this.base}/medicamento`, data)
      .pipe(map((item) => this.adaptMedicamento(item)));
  }

  atualizarMedicamento(data: MedicamentoRequest & { id?: number }): Observable<Medicamento> {
    return this.http
      .put<any>(`${this.base}/medicamento`, data)
      .pipe(map((item) => this.adaptMedicamento(item)));
  }

  toggleMedicamento(id: number): Observable<void> {
    return this.http.put<void>(`${this.base}/medicamento/inativar/${id}`, {});
  }

  // ── ATENDIMENTOS ───────────────────────────────────────────────────────────

  getAtendimentos(): Observable<Atendimento[]> {
    return this.http
      .get<any[]>(`${this.base}/atendimento`)
      .pipe(map((list) => list.map((item) => this.adaptAtendimento(item))));
  }

  // criarAtendimento(data: AtendimentoRequest): Observable<Atendimento> {
  //   const payload = {
  //     pacienteId: data.pacienteId,
  //     profissionalId: data.profissionalId,
  //     descricao: data.sintomas ?? data.descricao ?? "",
  //     observacoes: this.encodeAtendimentoDetails(data),
  //     dataAtendimento: data.dataInicio ?? data.dataAtendimento ?? "",
  //     status: data.tipo ?? data.status ?? "CONSULTA",
  //   };

  //   return this.http
  //     .post<any>(`${this.base}/atendimento`, payload)
  //     .pipe(map((item) => this.adaptAtendimento(item)));
  // }

  criarAtendimento(data: AtendimentoRequest): Observable<Atendimento> {
  const payload = {
    pacienteId: data.pacienteId,
    profissionalId: data.profissionalId,
    descricao: data.sintomas ?? data.descricao ?? "",
    observacoes: this.encodeAtendimentoDetails(data),
    dataAtendimento: data.dataInicio ?? data.dataAtendimento ?? "",
    status: data.tipo ?? data.status ?? "CONSULTA",
    medicacoesUsadas: (data.medicacoesUsadas ?? []).map((m) => ({
      medicacaoId: Number(m.medicacaoId),
      medicacaoNome: m.medicacaoNome ?? "",
      quantidade: Number(m.quantidade),
      dosagem: m.dosagem ?? "",
    })),
  };

  return this.http
    .post<any>(`${this.base}/atendimento`, payload)
    .pipe(map((item) => this.adaptAtendimento(item)));
}

  // atualizarAtendimento(data: AtendimentoRequest & { id?: number }): Observable<Atendimento> {
  //   const payload = {
  //     id: data.id,
  //     pacienteId: data.pacienteId,
  //     profissionalId: data.profissionalId,
  //     descricao: data.sintomas ?? data.descricao ?? "",
  //     observacoes: this.encodeAtendimentoDetails(data),
  //     dataAtendimento: data.dataInicio ?? data.dataAtendimento ?? "",
  //     status: data.tipo ?? data.status ?? "CONSULTA",
  //   };

  //   return this.http
  //     .put<any>(`${this.base}/atendimento`, payload)
  //     .pipe(map((item) => this.adaptAtendimento(item)));
  // }

  atualizarAtendimento(data: AtendimentoRequest & { id?: number }): Observable<Atendimento> {
  const payload = {
    id: data.id,
    pacienteId: data.pacienteId,
    profissionalId: data.profissionalId,
    descricao: data.sintomas ?? data.descricao ?? "",
    observacoes: this.encodeAtendimentoDetails(data),
    dataAtendimento: data.dataInicio ?? data.dataAtendimento ?? "",
    status: data.tipo ?? data.status ?? "CONSULTA",
    medicacoesUsadas: (data.medicacoesUsadas ?? []).map((m) => ({
      medicacaoId: Number(m.medicacaoId),
      medicacaoNome: m.medicacaoNome ?? "",
      quantidade: Number(m.quantidade),
      dosagem: m.dosagem ?? "",
    })),
  };

  return this.http
    .put<any>(`${this.base}/atendimento`, payload)
    .pipe(map((item) => this.adaptAtendimento(item)));
}

  // ── PRONTUÁRIOS ────────────────────────────────────────────────────────────

  getProntuarios(): Observable<Prontuario[]> {
    return this.http
      .get<any[]>(`${this.base}/prontuario`)
      .pipe(map((list) => list.map((item) => this.adaptProntuario(item))));
  }

  getProntuarioById(id: number): Observable<Prontuario> {
    return this.http
      .get<any>(`${this.base}/prontuario/${id}`)
      .pipe(map((item) => this.adaptProntuario(item)));
  }

  getProntuarioByPacienteId(id: number): Observable<Prontuario[]> {
    return this.http
      .get<any[]>(`${this.base}/prontuario/paciente/${id}`)
      .pipe(map((list) => list.map((item) => this.adaptProntuario(item))));
  }

  // ── REQUISIÇÕES ────────────────────────────────────────────────────────────

  getRequisicoes(): Observable<Requisicao[]> {
    return this.http.get<Requisicao[]>(`${this.base}/requisicoes`);
  }

  createRequisicao(payload: {
    medicacaoId: number;
    quantidade: number;
    tipo: string;
    profissionalId: number;
    data: string;
    observacao?: string;
  }): Observable<Requisicao> {
    return this.http.post<Requisicao>(`${this.base}/requisicoes`, payload);
  }

  // ── DASHBOARD / STATUS ─────────────────────────────────────────────────────

  getStatus(): Observable<StatusDashboard> {
    return this.http.get<StatusDashboard>(`${this.base}/status`);
  }
}