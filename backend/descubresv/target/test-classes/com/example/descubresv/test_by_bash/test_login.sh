#!/bin/bash

# Script para probar el endpoint de login de DescubreSV

# URL del login
URL="http://localhost:8080/api/auth/login"

# Lista de correos administradores a probar
declare -a CORREOS=(
    "pb24007@ues.edu.sv"
    "qp24002@ues.edu.sv"
    "rm24082@ues.edu.sv"
    "rf24006@ues.edu.sv"
    "rr24044@ues.edu.sv"
)

# Contraseña común para todos
PASSWORD="SuperUser-2026!"

echo "=========================================================="
echo "    PRUEBA DE INICIO DE SESIÓN (LOGIN) - DESCUBRESV       "
echo "=========================================================="
echo ""

# Iterar sobre cada correo y hacer una petición curl
for correo in "${CORREOS[@]}"
do
    echo "Probando login para: $correo"
    
    # Crear el JSON para el body
    JSON_BODY=$(cat <<EOF
{
  "correo": "$correo",
  "password": "$PASSWORD"
}
EOF
)

    # Hacer la peticion POST con curl, guardando los headers para ver la cookie y el status HTTP
    RESPONSE=$(curl -i -s -X POST "$URL" \
        -H "Content-Type: application/json" \
        -d "$JSON_BODY")
    
    # Extraer el codigo de estado HTTP de la primera linea
    HTTP_STATUS=$(echo "$RESPONSE" | head -n 1 | awk '{print $2}')
    
    if [ "$HTTP_STATUS" == "200" ]; then
        echo "✅ Éxito! Status HTTP: $HTTP_STATUS"
        # Opcional: imprimir una parte de la respuesta o la cookie JWT si la encuentra
        COOKIE=$(echo "$RESPONSE" | grep "Set-Cookie" | awk '{print $2}' | cut -d= -f1)
        if [ ! -z "$COOKIE" ]; then
            echo "   Cookie recibida: $COOKIE"
        fi
    else
        echo "❌ Falló! Status HTTP: $HTTP_STATUS"
        # Mostrar el body de error (ultima linea)
        echo "   Detalle: $(echo "$RESPONSE" | tail -n 1)"
    fi
    echo "----------------------------------------------------------"
done

echo "Proceso terminado."
