# Seldon Fleet

Aplicativo Android em Kotlin e Jetpack Compose para consultar a frota de `app/src/main/assets/frota.json`, classificar a saúde dos dispositivos e manter favoritos localmente.

## Executar

Abra o projeto no Android Studio e execute a configuração `app` em um emulador ou aparelho Android (minSdk 24). A frota é lida diretamente dos assets em cada inicialização. Não há chamadas HTTP nem banco de dados; os favoritos são persistidos com Preferences DataStore.

## Decisões

- `data` interpreta o JSON, converte mWh para mAh e fornece persistência; `domain` mantém modelos e classificação sem dependência Android.
- Registros que não permitem derivar o dreno permanecem na lista como **Indeterminado**.
- O detalhe explica o primeiro sinal que atingiu um limiar; voltar à lista mantém o filtro selecionado.
- A regra e a ameaça à validade estão descritas em [PROTOCOLO.md](PROTOCOLO.md).
- IA foi usada como apoio para estruturar a implementação e revisar os requisitos do desafio.

## Capturas

As capturas das telas ficaram pendentes: o AVD disponível iniciou como `offline` e desconectou antes da instalação do app. Não foram incluídas imagens de referência no lugar de capturas reais.

## Próximos passos

Capturar as duas telas em um dispositivo de referência quando o emulador estiver operacional.
