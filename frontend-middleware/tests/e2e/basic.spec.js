import { test, expect } from '@playwright/test'

test.describe('Pruebas Básicas', () => {
  test('debe cargar la aplicación correctamente', async ({ page }) => {
    // Verificar que podemos navegar a la aplicación
    await page.goto('/')
    
    // Verificar que el título contiene "Gestión de Tareas"
    await expect(page.locator('h1')).toContainText('Gestión de Tareas')
    
    // Verificar que hay elementos básicos presentes
    await expect(page.locator('body')).toBeVisible()
  })
})