#!/bin/bash
# Prueba para endpoints de Presupuestos (requiere saber ID de un itinerario, asumimos ID 1 para la prueba)

LOGIN_URL="http://localhost:8080/api/auth/login"
API_URL="http://localhost:8080/api/turista/itinerarios/1/presupuesto"
CORREO="rm24082@ues.edu.sv"
PASSWORD="SuperUser-2026!"

echo "--- PRUEBA DE PRESUPUESTO (Itinerario 1) ---"

# 1. Hacer login y guardar cookie
curl -s -c cookie.txt -X POST "$LOGIN_URL" \
    -H "Content-Type: application/json" \
    -d "{\"correo\":\"$CORREO\",\"password\":\"$PASSWORD\"}" > /dev/null

# 2. Extraer el token CSRF  
XSRF_TOKEN=$(grep -oP '(?<=XSRF-TOKEN\s)[^\s]*' cookie.txt | tail -n 1)

# 3. Peticion GET al presupuesto
echo "Haciendo GET a $API_URL..."
RESPONSE=$(curl -i -s -b cookie.txt -X GET "$API_URL" -H "X-XSRF-TOKEN: $XSRF_TOKEN")

HTTP_STATUS=$(echo "$RESPONSE" | head -n 1 | awk '{print $2}')

if [ "$HTTP_STATUS" == "200" ] || [ "$HTTP_STATUS" == "404" ] || [ "$HTTP_STATUS" == "403" ]; then
    echo "✅ Peticion completada. Status HTTP: $HTTP_STATUS (404/403 es normal si no existe o no es tuyo)"
else
    echo "❌ Falló! Status HTTP: $HTTP_STATUS"
fi

rm cookie.txt
