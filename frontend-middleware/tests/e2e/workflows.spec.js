import { test, expect } from '@playwright/test'

test.describe('Flujos Completos de Usuario', () => {
  test.beforeEach(async ({ page }) => {
    await page.goto('/')
    await page.waitForSelector('[data-testid="tasks-table"]', { timeout: 10000 })
  })

  test('flujo completo: crear, editar y eliminar tarea', async ({ page }) => {
    // 1. Contar tareas iniciales
    const initialCount = await page.locator('[data-testid="task-row"]').count()
    
    // 2. Crear nueva tarea
    await page.click('[data-testid="new-task-button"]')
    await expect(page).toHaveURL('/nueva')
    
    const titulo = 'Tarea Flujo Completo E2E'
    const descripcion = 'Esta tarea será creada, editada y eliminada en el test'
    
    await page.fill('[data-testid="titulo-input"]', titulo)
    await page.fill('[data-testid="descripcion-input"]', descripcion)
    await page.fill('[data-testid="fecha-input"]', '2025-03-01')
    await page.selectOption('[data-testid="estado-select"]', 'TODO')
    
    await page.click('[data-testid="submit-button"]')
    
    // Verificar creación exitosa
    await expect(page).toHaveURL('/')
    await expect(page.locator('[data-testid="toast"]')).toContainText('Tarea creada exitosamente')
    await expect(page.locator('[data-testid="task-row"]')).toHaveCount(initialCount + 1)
    
    // 3. Editar la tarea recién creada
    const nuevaTarea = page.locator('[data-testid="task-row"]').last()
    await nuevaTarea.locator('[data-testid="edit-button"]').click()
    
    const tituloEditado = titulo + ' - EDITADA'
    await nuevaTarea.locator('[data-testid="edit-titulo"]').fill(tituloEditado)
    await nuevaTarea.locator('[data-testid="edit-estado"]').selectOption('IN_PROGRESS')
    await nuevaTarea.locator('[data-testid="save-button"]').click()
    
    // Verificar edición exitosa
    await expect(nuevaTarea.locator('[data-testid="task-title"]')).toContainText('EDITADA')
    await expect(nuevaTarea.locator('[data-testid="status-badge"]')).toContainText('En progreso')
    
    // 4. Eliminar la tarea
    await nuevaTarea.locator('[data-testid="delete-button"]').click()
    await page.locator('[data-testid="confirm-delete"]').click()
    
    // Verificar eliminación exitosa
    await expect(page.locator('[data-testid="task-row"]')).toHaveCount(initialCount)
    await expect(page.locator('[data-testid="toast"]')).toContainText('Tarea eliminada')
  })

  test('flujo de filtrado y búsqueda avanzada', async ({ page }) => {
    // 1. Verificar estado inicial
    const totalTasks = await page.locator('[data-testid="task-row"]').count()
    expect(totalTasks).toBeGreaterThan(0)
    
    // 2. Filtrar por estado "Completadas"
    await page.selectOption('[data-testid="status-filter"]', 'DONE')
    await page.waitForTimeout(500)
    
    const completedTasks = await page.locator('[data-testid="task-row"]').count()
    expect(completedTasks).toBeLessThanOrEqual(totalTasks)
    
    // Verificar que todas las tareas mostradas están completadas
    const statusBadges = page.locator('[data-testid="status-badge"]')
    for (let i = 0; i < await statusBadges.count(); i++) {
      await expect(statusBadges.nth(i)).toContainText('Completada')
    }
    
    // 3. Limpiar filtro y buscar por texto
    await page.selectOption('[data-testid="status-filter"]', '')
    await page.fill('[data-testid="search-input"]', 'dashboard')
    await page.waitForTimeout(500)
    
    // Verificar resultados de búsqueda
    const searchResults = page.locator('[data-testid="task-row"]')
    const resultCount = await searchResults.count()
    
    if (resultCount > 0) {
      // Verificar que los resultados contienen el término buscado
      for (let i = 0; i < resultCount; i++) {
        const taskText = await searchResults.nth(i).textContent()
        expect(taskText.toLowerCase()).toContain('dashboard')
      }
    }
    
    // 4. Limpiar búsqueda
    await page.fill('[data-testid="search-input"]', '')
    await page.waitForTimeout(500)
    
    // Verificar que vuelven todas las tareas
    await expect(page.locator('[data-testid="task-row"]')).toHaveCount(totalTasks)
  })

  test('flujo de gestión de estados de tareas', async ({ page }) => {
    // 1. Encontrar una tarea en estado TODO
    await page.selectOption('[data-testid="status-filter"]', 'TODO')
    await page.waitForTimeout(500)
    
    const todoTasks = page.locator('[data-testid="task-row"]')
    const todoCount = await todoTasks.count()
    
    if (todoCount > 0) {
      // 2. Cambiar estado a IN_PROGRESS
      const firstTodo = todoTasks.first()
      await firstTodo.locator('[data-testid="edit-button"]').click()
      await firstTodo.locator('[data-testid="edit-estado"]').selectOption('IN_PROGRESS')
      await firstTodo.locator('[data-testid="save-button"]').click()
      
      // Verificar cambio de estado
      await expect(firstTodo.locator('[data-testid="status-badge"]')).toContainText('En progreso')
      
      // 3. Cambiar estado a DONE
      await firstTodo.locator('[data-testid="edit-button"]').click()
      await firstTodo.locator('[data-testid="edit-estado"]').selectOption('DONE')
      await firstTodo.locator('[data-testid="save-button"]').click()
      
      // Verificar estado final
      await expect(firstTodo.locator('[data-testid="status-badge"]')).toContainText('Completada')
      
      // 4. Verificar que el filtro funciona con el nuevo estado
      await page.selectOption('[data-testid="status-filter"]', 'DONE')
      await page.waitForTimeout(500)
      await expect(firstTodo).toBeVisible()
    }
  })

  test('flujo de navegación completa', async ({ page }) => {
    // 1. Verificar página principal
    await expect(page.locator('h1')).toContainText('Gestión de Tareas')
    
    // 2. Navegar a nueva tarea
    await page.click('[data-testid="new-task-button"]')
    await expect(page).toHaveURL('/nueva')
    await expect(page.locator('h1')).toContainText('Nueva Tarea')
    
    // 3. Volver con botón cancelar
    await page.click('[data-testid="cancel-button"]')
    await expect(page).toHaveURL('/')
    await expect(page.locator('h1')).toContainText('Gestión de Tareas')
    
    // 4. Navegar nuevamente y crear tarea
    await page.click('[data-testid="new-task-button"]')
    
    await page.fill('[data-testid="titulo-input"]', 'Tarea Navegación')
    await page.fill('[data-testid="descripcion-input"]', 'Probando navegación completa')
    await page.fill('[data-testid="fecha-input"]', '2025-02-28')
    
    await page.click('[data-testid="submit-button"]')
    
    // 5. Verificar retorno automático tras creación exitosa
    await expect(page).toHaveURL('/')
    await expect(page.locator('[data-testid="toast"]')).toContainText('Tarea creada exitosamente')
  })

  test('flujo de validación en tiempo real', async ({ page }) => {
    await page.goto('/nueva')
    await page.waitForSelector('[data-testid="new-task-form"]')
    
    // 1. Verificar validación en tiempo real del título
    await page.fill('[data-testid="titulo-input"]', 'A') // 1 carácter
    await page.click('[data-testid="descripcion-input"]') // Trigger blur
    await expect(page.locator('[data-testid="titulo-error"]')).toContainText('al menos 3 caracteres')
    
    // Corregir título
    await page.fill('[data-testid="titulo-input"]', 'Título válido')
    await page.click('[data-testid="descripcion-input"]')
    await expect(page.locator('[data-testid="titulo-error"]')).not.toBeVisible()
    
    // 2. Verificar validación de descripción
    await page.fill('[data-testid="descripcion-input"]', 'Corta') // Menos de 10 caracteres
    await page.click('[data-testid="titulo-input"]') // Trigger blur
    await expect(page.locator('[data-testid="descripcion-error"]')).toContainText('al menos 10 caracteres')
    
    // Corregir descripción
    await page.fill('[data-testid="descripcion-input"]', 'Descripción válida y suficientemente larga')
    await page.click('[data-testid="titulo-input"]')
    await expect(page.locator('[data-testid="descripcion-error"]')).not.toBeVisible()
    
    // 3. Verificar que el botón se habilita cuando el formulario es válido
    await page.fill('[data-testid="fecha-input"]', '2025-03-15')
    
    const submitButton = page.locator('[data-testid="submit-button"]')
    await expect(submitButton).not.toBeDisabled()
  })
})