#!/bin/bash

# Configuración Base
API_URL="http://localhost:8080/api"
COOKIE_FILE="cookies.txt"

# Colores para salida
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
CYAN='\033[0;36m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# Verifica si `jq` está instalado para formatear JSON
if ! command -v jq &> /dev/null; then
    echo -e "${RED}Nota: 'jq' no está instalado. El output JSON no se verá formateado.${NC}"
    echo -e "Puedes instalarlo con 'sudo apt install jq' en Linux.\n"
    FORMAT="cat"
else
    FORMAT="jq ."
fi

# ==========================================
# UTILIDADES
# ==========================================

get_csrf_token() {
    # Extrae el valor del token CSRF del archivo de cookies
    if [ -f "$COOKIE_FILE" ]; then
        awk '$6 == "XSRF-TOKEN" {print $7}' "$COOKIE_FILE"
    fi
}

ejecutar_get() {
    local endpoint=$1
    echo -e "${CYAN}Ejecutando GET a $endpoint...${NC}"
    curl -s -X GET "$API_URL$endpoint" -b "$COOKIE_FILE" | eval $FORMAT
}

ejecutar_post() {
    local endpoint=$1
    local json_data=$2
    local csrf=$(get_csrf_token)
    
    echo -e "${CYAN}Ejecutando POST a $endpoint...${NC}"
    curl -s -X POST "$API_URL$endpoint" \
        -b "$COOKIE_FILE" \
        -H "X-XSRF-TOKEN: $csrf" \
        -H "Content-Type: application/json" \
        -d "$json_data" | eval $FORMAT
}

ejecutar_put() {
    local endpoint=$1
    local json_data=$2
    local csrf=$(get_csrf_token)
    
    echo -e "${CYAN}Ejecutando PUT a $endpoint...${NC}"
    curl -s -X PUT "$API_URL$endpoint" \
        -b "$COOKIE_FILE" \
        -H "X-XSRF-TOKEN: $csrf" \
        -H "Content-Type: application/json" \
        -d "$json_data" | eval $FORMAT
}

ejecutar_delete() {
    local endpoint=$1
    local csrf=$(get_csrf_token)
    
    echo -e "${CYAN}Ejecutando DELETE a $endpoint...${NC}"
    curl -s -X DELETE "$API_URL$endpoint" \
        -b "$COOKIE_FILE" \
        -H "X-XSRF-TOKEN: $csrf" | eval $FORMAT
}

# ==========================================
# MÓDULOS DE ENTIDADES
# ==========================================

modulo_categorias() {
    while true; do
        echo -e "\n${YELLOW}--- GESTIÓN DE CATEGORÍAS ---${NC}"
        echo "1. Listar todas las categorías"
        echo "2. Crear nueva categoría"
        echo "3. Actualizar categoría"
        echo "4. Eliminar categoría"
        echo "0. Volver al menú principal"
        read -p "Seleccione una opción: " opt
        
        case $opt in
            1)
                ejecutar_get "/categorias"
                ;;
            2)
                read -p "Ingrese nombre de la categoría: " nombre
                read -p "Ingrese descripción: " descripcion
                ejecutar_post "/admin/categorias" "{\"nombreCategoria\":\"$nombre\", \"descripcion\":\"$descripcion\"}"
                ;;
            3)
                read -p "ID de la categoría a actualizar: " id_cat
                read -p "Nuevo nombre: " nombre
                read -p "Nueva descripción: " descripcion
                ejecutar_put "/admin/categorias/$id_cat" "{\"nombreCategoria\":\"$nombre\", \"descripcion\":\"$descripcion\"}"
                ;;
            4)
                read -p "ID de la categoría a eliminar: " id_cat
                ejecutar_delete "/admin/categorias/$id_cat"
                ;;
            0) break ;;
            *) echo -e "${RED}Opción no válida.${NC}" ;;
        esac
    done
}

modulo_destinos() {
    while true; do
        echo -e "\n${YELLOW}--- GESTIÓN DE DESTINOS (HOTELES, PLAYAS, ETC) ---${NC}"
        echo "1. Listar todos los destinos"
        echo "2. Crear nuevo destino"
        echo "3. Actualizar destino"
        echo "4. Eliminar destino"
        echo "0. Volver al menú principal"
        read -p "Seleccione una opción: " opt
        
        case $opt in
            1)
                ejecutar_get "/destinos"
                ;;
            2)
                echo "--- Ingrese los datos del nuevo destino ---"
                read -p "Nombre del destino (ej. Hotel Playa Linda): " nombre
                read -p "Descripción: " desc
                read -p "Departamento (Ubicación): " depto
                read -p "Precio Estimado / Tarifa ($): " precio
                read -p "Horario de atención: " horario
                read -p "Mejor época para visitar: " epoca
                read -p "Tipo (Hotel, Playa, Montaña...): " tipo
                read -p "ID de la Categoría: " id_cat
                
                # Armamos el JSON según DestinoRequest
                json=$(cat <<EOF
{
  "nombre": "$nombre",
  "descripcion": "$desc",
  "departamento": "$depto",
  "precioEntrada": $precio,
  "horario": "$horario",
  "mejorEpoca": "$epoca",
  "tipo": "$tipo",
  "idCategoria": $id_cat
}
EOF
)
                ejecutar_post "/admin/destinos" "$json"
                ;;
            3)
                read -p "ID del destino a actualizar: " id_dest
                echo "--- Ingrese los nuevos datos ---"
                read -p "Nombre: " nombre
                read -p "Descripción: " desc
                read -p "Departamento: " depto
                read -p "Precio ($): " precio
                read -p "Tipo: " tipo
                read -p "ID Categoría: " id_cat
                
                json=$(cat <<EOF
{
  "nombre": "$nombre",
  "descripcion": "$desc",
  "departamento": "$depto",
  "precioEntrada": $precio,
  "tipo": "$tipo",
  "idCategoria": $id_cat
}
EOF
)
                ejecutar_put "/admin/destinos/$id_dest" "$json"
                ;;
            4)
                read -p "ID del destino a eliminar: " id_dest
                ejecutar_delete "/admin/destinos/$id_dest"
                ;;
            0) break ;;
            *) echo -e "${RED}Opción no válida.${NC}" ;;
        esac
    done
}

modulo_usuarios() {
    while true; do
        echo -e "\n${YELLOW}--- GESTIÓN DE USUARIOS ---${NC}"
        echo "1. Listar usuarios"
        echo "2. Obtener usuario por ID"
        echo "3. Crear nuevo usuario (ADMIN o TURISTA)"
        echo "4. Actualizar usuario"
        echo "5. Desactivar (Eliminar) usuario"
        echo "0. Volver al menú principal"
        read -p "Seleccione una opción: " opt
        
        case $opt in
            1)
                ejecutar_get "/admin/usuarios"
                ;;
            2)
                read -p "ID del usuario: " id_user
                ejecutar_get "/admin/usuarios/$id_user"
                ;;
            3)
                echo "--- Ingrese los datos del usuario ---"
                read -p "Nombre: " nombre
                read -p "Correo: " correo
                read -s -p "Contraseña: " password; echo ""
                read -p "Nacionalidad: " nacion
                read -p "Rol (ADMIN o TURISTA): " rol
                
                # Default a mayúsculas
                rol=$(echo "$rol" | tr '[:lower:]' '[:upper:]')
                
                json=$(cat <<EOF
{
  "nombre": "$nombre",
  "correo": "$correo",
  "password": "$password",
  "nacionalidad": "$nacion",
  "rol": "$rol",
  "activo": true
}
EOF
)
                ejecutar_post "/admin/usuarios" "$json"
                ;;
            4)
                read -p "ID del usuario a actualizar: " id_user
                read -p "Nombre: " nombre
                read -p "Correo: " correo
                read -p "Nacionalidad: " nacion
                read -p "Rol (ADMIN o TURISTA): " rol
                
                rol=$(echo "$rol" | tr '[:lower:]' '[:upper:]')
                
                json=$(cat <<EOF
{
  "nombre": "$nombre",
  "correo": "$correo",
  "nacionalidad": "$nacion",
  "rol": "$rol",
  "activo": true
}
EOF
)
                ejecutar_put "/admin/usuarios/$id_user" "$json"
                ;;
            5)
                read -p "ID del usuario a desactivar: " id_user
                ejecutar_delete "/admin/usuarios/$id_user"
                ;;
            0) break ;;
            *) echo -e "${RED}Opción no válida.${NC}" ;;
        esac
    done
}

modulo_auth_perfil() {
    while true; do
        echo -e "\n${YELLOW}--- MI PERFIL ---${NC}"
        echo "1. Ver mis datos (Perfil)"
        echo "0. Volver al menú principal"
        read -p "Seleccione una opción: " opt
        
        case $opt in
            1) ejecutar_get "/auth/perfil" ;;
            0) break ;;
            *) echo -e "${RED}Opción no válida.${NC}" ;;
        esac
    done
}

# ==========================================
# FLUJO PRINCIPAL
# ==========================================

login() {
    echo -e "${YELLOW}=== BIENVENIDO A DESCUBRE-SV CLI ===${NC}"
    echo "Inicie sesión con sus credenciales de Administrador."
    
    # Credenciales por defecto (tu usuario)
    read -p "Correo [rm24082@ues.edu.sv]: " email
    email=${email:-rm24082@ues.edu.sv}
    
    read -s -p "Contraseña [SuperUser-2026!]: " password
    password=${password:-SuperUser-2026!}
    echo -e "\nAutenticando..."

    # Hacer peticion y guardar cookie
    HTTP_STATUS=$(curl -s -o /dev/null -w "%{http_code}" -X POST "$API_URL/auth/login" \
        -H "Content-Type: application/json" \
        -d "{\"correo\":\"$email\", \"password\":\"$password\"}" \
        -c "$COOKIE_FILE")

    if [ "$HTTP_STATUS" -eq 200 ]; then
        echo -e "${GREEN}¡Login exitoso! Sesión iniciada como Admin.${NC}"
    else
        echo -e "${RED}Error en login. Código HTTP: $HTTP_STATUS${NC}"
        echo "Asegúrese de que el backend esté corriendo y la base de datos tenga los seeders."
        exit 1
    fi
}

menu_principal() {
    while true; do
        echo -e "\n${GREEN}=== MENÚ PRINCIPAL ===${NC}"
        echo "1. Gestionar Usuarios (Admins/Turistas)"
        echo "2. Gestionar Categorías"
        echo "3. Gestionar Destinos (Hoteles, Playas, etc.)"
        echo "4. Ver Mi Perfil"
        echo "0. Cerrar Sesión y Salir"
        read -p "Seleccione el módulo: " modulo

        case $modulo in
            1) modulo_usuarios ;;
            2) modulo_categorias ;;
            3) modulo_destinos ;;
            4) modulo_auth_perfil ;;
            0) 
                echo "Cerrando sesión..."
                # Llamada opcional de logout al API
                curl -s -X POST "$API_URL/auth/logout" -b "$COOKIE_FILE" > /dev/null
                rm -f "$COOKIE_FILE"
                echo -e "${GREEN}¡Hasta pronto!${NC}"
                exit 0 
                ;;
            *) echo -e "${RED}Opción no válida.${NC}" ;;
        esac
    done
}

# Iniciar la aplicación de consola
rm -f "$COOKIE_FILE" # Limpia sesiones previas
login
menu_principal
