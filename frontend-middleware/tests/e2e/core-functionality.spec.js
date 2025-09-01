import { test, expect } from '@playwright/test'

test.describe('Funcionalidad Core de la Aplicación', () => {
  test.beforeEach(async ({ page }) => {
    await page.goto('/')
    // Esperar a que la aplicación cargue completamente
    await page.waitForSelector('h1', { timeout: 10000 })
  })

  test('debe cargar la página principal con elementos básicos', async ({ page }) => {
    // Verificar título
    await expect(page.locator('h1')).toContainText('Gestión de Tareas')
    
    // Verificar que hay contenido principal
    await expect(page.locator('main')).toBeVisible()
    
    // Verificar botón de nueva tarea
    await expect(page.locator('text=Nueva Tarea')).toBeVisible()
  })

  test('debe navegar entre páginas correctamente', async ({ page }) => {
    // Ir a nueva tarea
    await page.click('text=Nueva Tarea')
    await expect(page).toHaveURL('/nueva')
    await expect(page.locator('h1')).toContainText('Nueva Tarea')
    
    // Volver a la página principal
    await page.click('text=Volver al listado')
    await expect(page).toHaveURL('/')
    await expect(page.locator('h1')).toContainText('Gestión de Tareas')
  })

  test('debe mostrar y usar el formulario de nueva tarea', async ({ page }) => {
    // Navegar a nueva tarea
    await page.click('text=Nueva Tarea')
    
    // Verificar campos del formulario
    await expect(page.locator('input[name="titulo"]')).toBeVisible()
    await expect(page.locator('textarea[name="descripcion"]')).toBeVisible()
    await expect(page.locator('input[name="fecha"]')).toBeVisible()
    await expect(page.locator('select[name="estado"]')).toBeVisible()
    
    // Llenar formulario básico
    await page.fill('input[name="titulo"]', 'Test E2E Básico')
    await page.fill('textarea[name="descripcion"]', 'Esta es una tarea de prueba para E2E testing básico')
    await page.fill('input[name="fecha"]', '2025-02-15')
    
    // Enviar formulario
    await page.click('button[type="submit"]')
    
    // Verificar redirección
    await expect(page).toHaveURL('/')
  })

  test('debe mostrar datos mock cuando esté disponible', async ({ page }) => {
    // Esperar a que los datos carguen
    await page.waitForTimeout(2000)
    
    // Verificar que hay contenido de tareas (puede ser tabla, lista, etc.)
    const hasTaskContent = await page.locator('text=Implementar').count() > 0 ||
                          await page.locator('text=Configurar').count() > 0 ||
                          await page.locator('text=Por Hacer').count() > 0
    
    expect(hasTaskContent).toBeTruthy()
  })

  test('debe ser responsive en móvil', async ({ page }) => {
    // Configurar viewport móvil
    await page.setViewportSize({ width: 375, height: 667 })
    
    // Verificar elementos principales siguen visibles
    await expect(page.locator('h1')).toBeVisible()
    await expect(page.locator('text=Nueva Tarea')).toBeVisible()
    
    // Navegar a nueva tarea en móvil
    await page.click('text=Nueva Tarea')
    await expect(page.locator('h1')).toContainText('Nueva Tarea')
    
    // Verificar formulario es usable en móvil
    await expect(page.locator('input[name="titulo"]')).toBeVisible()
    await expect(page.locator('button[type="submit"]')).toBeVisible()
  })
})