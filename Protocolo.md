# Protocolo de decisões — Seldon Fleet

## Regra de classificação adotada

O domínio classifica cada aparelho pelas derivadas de consumo e pelos sinais de falhas e quadros perdidos. O dreno absoluto é `consumo_mAh / (tela_ligada_min / 60)`; o relativo é `(dreno_absoluto / capacidade_mAh) × 100`. A precedência é **Indeterminado**, **Crítico**, **Atenção**, **OK**. Crítico: dreno relativo ≥ 12 %/h, falhas em 7 dias ≥ 3 ou jank ≥ 15 %. Atenção: dreno relativo ≥ 7 %/h, falhas ≥ 1 ou jank ≥ 8 %. Se nenhum sinal disparar, o estado é OK. Dentro de cada estado a justificativa apresenta o primeiro sinal que atingiu o limiar.

## Defeito 1 — unidades de energia misturadas

`energyConsumed` só é convertido na fronteira de dados: valores em mWh são divididos pela tensão nominal em volts para produzir mAh; valores já em mAh permanecem iguais. O modelo de domínio recebe apenas mAh. Unidade desconhecida ou tensão ausente/inválida produz energia indisponível e, portanto, classificação indeterminada. Por exemplo, 3773 mWh ÷ 3,85 V ≈ 980 mAh.

## Defeito 2 — divisor nulo nas derivadas

Escolhi manter o registro na frota e classificá-lo como **Indeterminado** quando a capacidade estiver ausente ou ≤ 0, o tempo de tela estiver ausente ou ≤ 0, ou a energia normalizada não puder ser calculada. Assim o operador ainda consegue consultar os outros atributos do aparelho, enquanto a regra evita divisão por zero, `NaN`, `Infinity` e encerramento do app.

## Ameaça à validade

O dreno usa o consumo acumulado do período registrado dividido pelo tempo de tela. Se o aparelho executou serviços em primeiro plano com a tela desligada, ou se o período de `energyConsumed` não corresponder ao período de `screenOnMinutes`, o valor pode superestimar ou subestimar o dreno e produzir um falso alerta. A fonte local também pode estar desatualizada.
