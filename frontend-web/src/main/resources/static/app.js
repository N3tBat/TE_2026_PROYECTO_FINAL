document.addEventListener('DOMContentLoaded', () => {
    
    // Elementos de la UI
    const selectorContainer = document.getElementById('user-selector');
    const studentForm = document.getElementById('student-form');
    const professorForm = document.getElementById('professor-form');
    const adminForm = document.getElementById('admin-form');
    
    const cardAlumno = document.getElementById('card-alumno');
    const cardProfesor = document.getElementById('card-profesor');
    const btnAdminLogin = document.getElementById('btn-admin-login');
    
    const backButtons = document.querySelectorAll('.back-btn');

    // Función para ocultar todos los elementos interactivos
    function hideAll() {
        selectorContainer.style.display = 'none';
        studentForm.style.display = 'none';
        professorForm.style.display = 'none';
        adminForm.style.display = 'none';
        
        // Limpiar errores al cambiar de vista
        document.querySelectorAll('.error-msg').forEach(el => el.style.display = 'none');
    }

    // Mostrar el formulario de alumno
    cardAlumno.addEventListener('click', () => {
        hideAll();
        studentForm.style.display = 'block';
    });

    // Mostrar el formulario de profesor
    cardProfesor.addEventListener('click', () => {
        hideAll();
        professorForm.style.display = 'block';
    });

    // Mostrar el formulario de admin
    btnAdminLogin.addEventListener('click', () => {
        hideAll();
        adminForm.style.display = 'block';
    });

    // Botones de "Volver"
    backButtons.forEach(btn => {
        btn.addEventListener('click', () => {
            hideAll();
            selectorContainer.style.display = 'flex';
        });
    });

    // --- LÓGICA DE AUTENTICACIÓN HACIA EL BACKEND ---
    const apiUrl = 'http://localhost:8080/api/auth/login';

    async function handleLogin(event, roleFormId, accountInputId, passwordInputId, errorDivId) {
        event.preventDefault();
        
        const account = document.getElementById(accountInputId).value;
        const password = document.getElementById(passwordInputId).value;
        const errorDiv = document.getElementById(errorDivId);
        
        errorDiv.style.display = 'none'; // ocultar error previo

        try {
            const response = await fetch(apiUrl, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    correo: account,
                    password: password
                })
            });

            if (response.ok) {
                const data = await response.json();
                
                // Guardamos el token y rol devuelto por la API
                localStorage.setItem('jwt_token', data.token);
                localStorage.setItem('user_role', data.rol);
                
                // Redirigir según el rol devuelto por el Backend
                if (data.rol === 'ADMIN') {
                    window.location.href = '/admin.html';
                } else if (data.rol === 'PROFESOR') {
                    window.location.href = '/profesor.html';
                } else {
                    window.location.href = '/alumno.html';
                }
                
            } else {
                const errorMessage = await response.text();
                errorDiv.textContent = 'Error: Credenciales incorrectas';
                errorDiv.style.display = 'block';
            }
            
        } catch (error) {
            console.error('Error al conectar con la API:', error);
            errorDiv.textContent = 'Error de conexión con el servidor.';
            errorDiv.style.display = 'block';
        }
    }

    // Asignar los eventos de submit a cada formulario
    studentForm.addEventListener('submit', (e) => handleLogin(e, 'student-form', 'student-account', 'student-password', 'student-error'));
    professorForm.addEventListener('submit', (e) => handleLogin(e, 'professor-form', 'professor-account', 'professor-password', 'professor-error'));
    adminForm.addEventListener('submit', (e) => handleLogin(e, 'admin-form', 'admin-account', 'admin-password', 'admin-error'));
    
    // Soporte para Enter key en las tarjetas para accesibilidad
    cardAlumno.addEventListener('keypress', (e) => { if (e.key === 'Enter') cardAlumno.click(); });
    cardProfesor.addEventListener('keypress', (e) => { if (e.key === 'Enter') cardProfesor.click(); });
});
