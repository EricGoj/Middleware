import { test, expect } from '@playwright/test'

test.describe('Crear Nueva Tarea', () => {
  test.beforeEach(async ({ page }) => {
    // Navegar a la página de nueva tarea
    await page.goto('/nueva')
    
    // Esperar a que el formulario cargue
    await page.waitForSelector('[data-testid="new-task-form"]', { timeout: 10000 })
  })

  test('debe cargar el formulario correctamente', async ({ page }) => {
    // Verificar título de la página
    await expect(page.locator('h1')).toContainText('Nueva Tarea')
    
    // Verificar campos del formulario
    await expect(page.locator('[data-testid="titulo-input"]')).toBeVisible()
    await expect(page.locator('[data-testid="descripcion-input"]')).toBeVisible()
    await expect(page.locator('[data-testid="fecha-input"]')).toBeVisible()
    await expect(page.locator('[data-testid="estado-select"]')).toBeVisible()
    
    // Verificar botones
    await expect(page.locator('[data-testid="submit-button"]')).toBeVisible()
    await expect(page.locator('[data-testid="cancel-button"]')).toBeVisible()
  })

  test('debe validar campos requeridos', async ({ page }) => {
    // Intentar enviar formulario vacío
    await page.click('[data-testid="submit-button"]')
    
    // Verificar mensajes de error
    await expect(page.locator('[data-testid="titulo-error"]')).toContainText('El título es obligatorio')
    await expect(page.locator('[data-testid="descripcion-error"]')).toContainText('La descripción es obligatoria')
  })

  test('debe validar longitud mínima de campos', async ({ page }) => {
    // Llenar campos con texto muy corto
    await page.fill('[data-testid="titulo-input"]', 'AB') // Menos de 3 caracteres
    await page.fill('[data-testid="descripcion-input"]', 'Desc') // Menos de 10 caracteres
    
    await page.click('[data-testid="submit-button"]')
    
    // Verificar mensajes de error específicos
    await expect(page.locator('[data-testid="titulo-error"]')).toContainText('al menos 3 caracteres')
    await expect(page.locator('[data-testid="descripcion-error"]')).toContainText('al menos 10 caracteres')
  })

  test('debe crear una nueva tarea exitosamente', async ({ page }) => {
    // Llenar formulario con datos válidos
    const nuevoTitulo = 'Tarea E2E Test'
    const nuevaDescripcion = 'Esta tarea fue creada desde los tests E2E automatizados'
    const fechaVencimiento = '2025-02-15'
    
    await page.fill('[data-testid="titulo-input"]', nuevoTitulo)
    await page.fill('[data-testid="descripcion-input"]', nuevaDescripcion)
    await page.fill('[data-testid="fecha-input"]', fechaVencimiento)
    await page.selectOption('[data-testid="estado-select"]', 'TODO')
    
    // Enviar formulario
    await page.click('[data-testid="submit-button"]')
    
    // Verificar redirección a página principal
    await expect(page).toHaveURL('/')
    
    // Verificar que se muestra el toast de éxito
    await expect(page.locator('[data-testid="toast"]')).toContainText('Tarea creada exitosamente')
    
    // Verificar que la nueva tarea aparece en la tabla
    await expect(page.locator('[data-testid="task-row"]').last()).toContainText(nuevoTitulo)
  })

  test('debe manejar errores de creación', async ({ page }) => {
    // Simular error llenando un formulario y luego interceptando la respuesta
    await page.route('**/api/tasks', (route) => {
      route.fulfill({
        status: 500,
        body: JSON.stringify({ message: 'Error interno del servidor' })
      })
    })
    
    // Llenar formulario
    await page.fill('[data-testid="titulo-input"]', 'Tarea con error')
    await page.fill('[data-testid="descripcion-input"]', 'Esta tarea debería fallar')
    await page.fill('[data-testid="fecha-input"]', '2025-02-15')
    
    // Enviar formulario
    await page.click('[data-testid="submit-button"]')
    
    // Verificar que se muestra el mensaje de error
    await expect(page.locator('[data-testid="toast"]')).toContainText('Error al crear la tarea')
    
    // Verificar que permanecemos en la página de nueva tarea
    await expect(page).toHaveURL('/nueva')
  })

  test('debe cancelar la creación y volver a la página principal', async ({ page }) => {
    // Llenar parcialmente el formulario
    await page.fill('[data-testid="titulo-input"]', 'Tarea cancelada')
    
    // Hacer clic en cancelar
    await page.click('[data-testid="cancel-button"]')
    
    // Verificar redirección
    await expect(page).toHaveURL('/')
    
    // Verificar que volvemos a la tabla de tareas
    await expect(page.locator('[data-testid="tasks-table"]')).toBeVisible()
  })

  test('debe ser accesible con navegación por teclado', async ({ page }) => {
    // Navegar con Tab por todos los elementos
    await page.keyboard.press('Tab') // Título input
    await expect(page.locator('[data-testid="titulo-input"]')).toBeFocused()
    
    await page.keyboard.press('Tab') // Descripción input
    await expect(page.locator('[data-testid="descripcion-input"]')).toBeFocused()
    
    await page.keyboard.press('Tab') // Fecha input
    await expect(page.locator('[data-testid="fecha-input"]')).toBeFocused()
    
    await page.keyboard.press('Tab') // Estado select
    await expect(page.locator('[data-testid="estado-select"]')).toBeFocused()
    
    // Llenar formulario usando solo teclado
    await page.keyboard.type('Tarea creada con teclado')
    await page.keyboard.press('Tab')
    await page.keyboard.type('Descripción creada navegando solo con teclado para probar accesibilidad')
    
    // Enviar con Enter
    await page.keyboard.press('Tab') // Mover a botón submit
    await page.keyboard.press('Tab') // Mover a botón submit
    await page.keyboard.press('Enter')
    
    // Verificar éxito
    await expect(page).toHaveURL('/')
  })

  test('debe funcionar correctamente en dispositivos móviles', async ({ page }) => {
    // Configurar viewport móvil
    await page.setViewportSize({ width: 375, height: 667 })
    
    // Verificar que el formulario es responsive
    const form = page.locator('[data-testid="new-task-form"]')
    await expect(form).toBeVisible()
    
    // Verificar que los campos son accesibles en móvil
    await expect(page.locator('[data-testid="titulo-input"]')).toBeVisible()
    await expect(page.locator('[data-testid="descripcion-input"]')).toBeVisible()
    
    // Crear tarea en móvil
    await page.fill('[data-testid="titulo-input"]', 'Tarea móvil')
    await page.fill('[data-testid="descripcion-input"]', 'Tarea creada desde dispositivo móvil')
    await page.fill('[data-testid="fecha-input"]', '2025-02-20')
    
    await page.click('[data-testid="submit-button"]')
    
    // Verificar éxito
    await expect(page).toHaveURL('/')
    await expect(page.locator('[data-testid="toast"]')).toContainText('Tarea creada exitosamente')
  })
})