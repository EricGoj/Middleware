import { test, expect } from '@playwright/test'

test.describe('Gestión de Tareas', () => {
  test.beforeEach(async ({ page }) => {
    // Navegar a la página principal
    await page.goto('/')
    
    // Esperar a que la aplicación cargue
    await page.waitForSelector('[data-testid="tasks-table"]', { timeout: 10000 })
  })

  test('debe cargar la página principal correctamente', async ({ page }) => {
    // Verificar título de la página
    await expect(page).toHaveTitle(/Gestión de Tareas/)
    
    // Verificar elementos principales
    await expect(page.locator('h1')).toContainText('Gestión de Tareas')
    await expect(page.locator('[data-testid="integration-status"]')).toBeVisible()
    await expect(page.locator('[data-testid="tasks-table"]')).toBeVisible()
    
    // Verificar que hay tareas mock cargadas
    const taskRows = page.locator('[data-testid="task-row"]')
    await expect(taskRows).toHaveCount(8) // Según mockTasks
  })

  test('debe mostrar información de integración', async ({ page }) => {
    const integrationStatus = page.locator('[data-testid="integration-status"]')
    
    // Verificar estado de conexión
    await expect(integrationStatus.locator('.text-green-600')).toContainText('Conectado')
    
    // Verificar información de sincronización
    await expect(integrationStatus).toContainText('Última sincronización')
    await expect(integrationStatus).toContainText('8 tareas')
  })

  test('debe filtrar tareas por estado', async ({ page }) => {
    // Verificar filtro inicial (todas las tareas)
    let taskRows = page.locator('[data-testid="task-row"]')
    await expect(taskRows).toHaveCount(8)
    
    // Filtrar por "En progreso"
    await page.selectOption('[data-testid="status-filter"]', 'IN_PROGRESS')
    await page.waitForTimeout(500) // Esperar a que se aplique el filtro
    
    taskRows = page.locator('[data-testid="task-row"]')
    await expect(taskRows).toHaveCount(2) // Tareas en progreso en mock data
    
    // Verificar que todas las tareas mostradas tienen el estado correcto
    const statusBadges = page.locator('[data-testid="status-badge"]')
    for (let i = 0; i < await statusBadges.count(); i++) {
      await expect(statusBadges.nth(i)).toContainText('En progreso')
    }
  })

  test('debe buscar tareas por texto', async ({ page }) => {
    // Buscar por palabra clave
    await page.fill('[data-testid="search-input"]', 'autenticación')
    await page.waitForTimeout(500)
    
    // Verificar que se muestra solo la tarea que coincide
    const taskRows = page.locator('[data-testid="task-row"]')
    await expect(taskRows).toHaveCount(1)
    await expect(taskRows.first()).toContainText('autenticación')
  })

  test('debe editar una tarea inline', async ({ page }) => {
    // Hacer clic en el botón de editar de la primera tarea
    const firstTaskRow = page.locator('[data-testid="task-row"]').first()
    await firstTaskRow.locator('[data-testid="edit-button"]').click()
    
    // Verificar que aparecen los campos de edición
    await expect(firstTaskRow.locator('[data-testid="edit-titulo"]')).toBeVisible()
    await expect(firstTaskRow.locator('[data-testid="edit-descripcion"]')).toBeVisible()
    
    // Modificar el título
    const nuevoTitulo = 'Título editado desde E2E test'
    await firstTaskRow.locator('[data-testid="edit-titulo"]').fill(nuevoTitulo)
    
    // Guardar cambios
    await firstTaskRow.locator('[data-testid="save-button"]').click()
    
    // Verificar que el cambio se guardó
    await expect(firstTaskRow.locator('[data-testid="task-title"]')).toContainText(nuevoTitulo)
  })

  test('debe eliminar una tarea con confirmación', async ({ page }) => {
    // Contar tareas iniciales
    const initialTaskCount = await page.locator('[data-testid="task-row"]').count()
    
    // Hacer clic en el botón de eliminar de la primera tarea
    const firstTaskRow = page.locator('[data-testid="task-row"]').first()
    await firstTaskRow.locator('[data-testid="delete-button"]').click()
    
    // Verificar que aparece el modal de confirmación
    const modal = page.locator('[data-testid="delete-modal"]')
    await expect(modal).toBeVisible()
    await expect(modal).toContainText('¿Estás seguro?')
    
    // Confirmar eliminación
    await modal.locator('[data-testid="confirm-delete"]').click()
    
    // Verificar que la tarea se eliminó
    await expect(page.locator('[data-testid="task-row"]')).toHaveCount(initialTaskCount - 1)
    
    // Verificar que se muestra el toast de confirmación
    await expect(page.locator('[data-testid="toast"]')).toContainText('Tarea eliminada')
  })

  test('debe navegar a la página de nueva tarea', async ({ page }) => {
    // Hacer clic en el botón de nueva tarea
    await page.click('[data-testid="new-task-button"]')
    
    // Verificar navegación
    await expect(page).toHaveURL('/nueva')
    await expect(page.locator('h1')).toContainText('Nueva Tarea')
  })

  test('debe funcionar en dispositivos móviles', async ({ page }) => {
    // Cambiar a viewport móvil
    await page.setViewportSize({ width: 375, height: 667 })
    
    // Verificar que la tabla es responsive
    const table = page.locator('[data-testid="tasks-table"]')
    await expect(table).toBeVisible()
    
    // Verificar que los botones son accesibles en móvil
    const newTaskButton = page.locator('[data-testid="new-task-button"]')
    await expect(newTaskButton).toBeVisible()
    
    // Verificar que el filtro funciona en móvil
    await page.selectOption('[data-testid="status-filter"]', 'DONE')
    await page.waitForTimeout(500)
    
    const completedTasks = page.locator('[data-testid="task-row"]')
    await expect(completedTasks).toHaveCount(1) // Una tarea completada en mock data
  })
})