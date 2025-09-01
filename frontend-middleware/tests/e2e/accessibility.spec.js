import { test, expect } from '@playwright/test'

test.describe('Pruebas de Accesibilidad', () => {
  test.beforeEach(async ({ page }) => {
    await page.goto('/')
    await page.waitForSelector('[data-testid="tasks-table"]', { timeout: 10000 })
  })

  test('debe tener estructura semántica correcta', async ({ page }) => {
    // Verificar elementos semánticos principales
    await expect(page.locator('main')).toBeVisible()
    await expect(page.locator('h1')).toBeVisible()
    
    // Verificar que la tabla tiene estructura accesible
    const table = page.locator('[data-testid="tasks-table"]')
    await expect(table.locator('thead')).toBeVisible()
    await expect(table.locator('tbody')).toBeVisible()
    
    // Verificar headers de tabla
    const headers = table.locator('th')
    await expect(headers).toHaveCount(5) // Título, Descripción, Fecha, Estado, Acciones
  })

  test('debe tener labels y aria-labels apropiados', async ({ page }) => {
    // Verificar filtros y búsqueda
    const statusFilter = page.locator('[data-testid="status-filter"]')
    await expect(statusFilter).toHaveAttribute('aria-label', /filtrar.*estado/i)
    
    const searchInput = page.locator('[data-testid="search-input"]')
    await expect(searchInput).toHaveAttribute('placeholder', /buscar/i)
    
    // Verificar botones tienen labels descriptivos
    const newTaskButton = page.locator('[data-testid="new-task-button"]')
    await expect(newTaskButton).toContainText('Nueva Tarea')
  })

  test('debe ser navegable por teclado', async ({ page }) => {
    // Verificar navegación por Tab
    await page.keyboard.press('Tab') // Search input
    await expect(page.locator('[data-testid="search-input"]')).toBeFocused()
    
    await page.keyboard.press('Tab') // Status filter
    await expect(page.locator('[data-testid="status-filter"]')).toBeFocused()
    
    await page.keyboard.press('Tab') // New task button
    await expect(page.locator('[data-testid="new-task-button"]')).toBeFocused()
    
    // Verificar que se puede activar el botón con Enter
    await page.keyboard.press('Enter')
    await expect(page).toHaveURL('/nueva')
  })

  test('debe manejar focus correctamente en formularios', async ({ page }) => {
    await page.goto('/nueva')
    await page.waitForSelector('[data-testid="new-task-form"]')
    
    // Verificar orden de tab en formulario
    await page.keyboard.press('Tab')
    await expect(page.locator('[data-testid="titulo-input"]')).toBeFocused()
    
    await page.keyboard.press('Tab')
    await expect(page.locator('[data-testid="descripcion-input"]')).toBeFocused()
    
    await page.keyboard.press('Tab')
    await expect(page.locator('[data-testid="fecha-input"]')).toBeFocused()
    
    await page.keyboard.press('Tab')
    await expect(page.locator('[data-testid="estado-select"]')).toBeFocused()
  })

  test('debe tener contraste adecuado en diferentes estados', async ({ page }) => {
    // Verificar badges de estado son visibles
    const statusBadges = page.locator('[data-testid="status-badge"]')
    
    for (let i = 0; i < Math.min(await statusBadges.count(), 3); i++) {
      const badge = statusBadges.nth(i)
      await expect(badge).toBeVisible()
      
      // Verificar que tiene color de fondo y texto
      const bgColor = await badge.evaluate(el => getComputedStyle(el).backgroundColor)
      const textColor = await badge.evaluate(el => getComputedStyle(el).color)
      
      expect(bgColor).not.toBe('rgba(0, 0, 0, 0)') // No transparente
      expect(textColor).not.toBe('rgba(0, 0, 0, 0)') // No transparente
    }
  })

  test('debe mostrar feedback apropiado para acciones del usuario', async ({ page }) => {
    // 1. Verificar feedback de creación de tarea
    await page.click('[data-testid="new-task-button"]')
    
    await page.fill('[data-testid="titulo-input"]', 'Tarea con Feedback')
    await page.fill('[data-testid="descripcion-input"]', 'Probando feedback visual')
    await page.fill('[data-testid="fecha-input"]', '2025-03-10')
    
    await page.click('[data-testid="submit-button"]')
    
    // Verificar toast de éxito
    const successToast = page.locator('[data-testid="toast"]')
    await expect(successToast).toBeVisible()
    await expect(successToast).toContainText('Tarea creada exitosamente')
    
    // 2. Verificar feedback de eliminación
    const firstTask = page.locator('[data-testid="task-row"]').first()
    await firstTask.locator('[data-testid="delete-button"]').click()
    
    // Verificar modal de confirmación
    const modal = page.locator('[data-testid="delete-modal"]')
    await expect(modal).toBeVisible()
    await expect(modal).toContainText('¿Estás seguro?')
    
    await modal.locator('[data-testid="confirm-delete"]').click()
    
    // Verificar toast de eliminación
    await expect(page.locator('[data-testid="toast"]')).toContainText('Tarea eliminada')
  })

  test('debe ser responsive en diferentes tamaños de pantalla', async ({ page }) => {
    const viewports = [
      { width: 1920, height: 1080, name: 'Desktop Large' },
      { width: 1366, height: 768, name: 'Desktop Standard' },
      { width: 768, height: 1024, name: 'Tablet' },
      { width: 375, height: 667, name: 'Mobile' }
    ]
    
    for (const viewport of viewports) {
      await page.setViewportSize({ width: viewport.width, height: viewport.height })
      
      // Verificar elementos principales siguen siendo visibles
      await expect(page.locator('h1')).toBeVisible()
      await expect(page.locator('[data-testid="tasks-table"]')).toBeVisible()
      await expect(page.locator('[data-testid="new-task-button"]')).toBeVisible()
      
      // Verificar que los controles de filtro son accesibles
      await expect(page.locator('[data-testid="search-input"]')).toBeVisible()
      await expect(page.locator('[data-testid="status-filter"]')).toBeVisible()
      
      // En móvil, verificar que la tabla sigue siendo usable
      if (viewport.width <= 768) {
        const firstTask = page.locator('[data-testid="task-row"]').first()
        if (await firstTask.count() > 0) {
          await expect(firstTask.locator('[data-testid="edit-button"]')).toBeVisible()
          await expect(firstTask.locator('[data-testid="delete-button"]')).toBeVisible()
        }
      }
    }
  })

  test('debe manejar errores de red gracefulmente', async ({ page }) => {
    // Simular error de red para getTasks
    await page.route('**/api/tasks', (route) => {
      route.abort('failed')
    })
    
    // Recargar página para triggear error
    await page.reload()
    await page.waitForTimeout(2000)
    
    // Verificar que se muestra un mensaje de error apropiado
    // (En la implementación real, esto dependería de cómo manejemos errores de carga)
    const errorElement = page.locator('[data-testid="error-message"], [data-testid="toast"]')
    await expect(errorElement).toBeVisible({ timeout: 5000 })
  })

  test('debe mantener estado durante la navegación', async ({ page }) => {
    // 1. Aplicar filtros
    await page.fill('[data-testid="search-input"]', 'configurar')
    await page.selectOption('[data-testid="status-filter"]', 'TODO')
    await page.waitForTimeout(500)
    
    const filteredCount = await page.locator('[data-testid="task-row"]').count()
    
    // 2. Navegar a nueva tarea
    await page.click('[data-testid="new-task-button"]')
    await expect(page).toHaveURL('/nueva')
    
    // 3. Volver a página principal
    await page.click('[data-testid="cancel-button"]')
    await expect(page).toHaveURL('/')
    
    // 4. Verificar que los filtros se mantuvieron
    await expect(page.locator('[data-testid="search-input"]')).toHaveValue('configurar')
    await expect(page.locator('[data-testid="status-filter"]')).toHaveValue('TODO')
    
    // Verificar que el conteo de tareas es el mismo
    await expect(page.locator('[data-testid="task-row"]')).toHaveCount(filteredCount)
  })
})